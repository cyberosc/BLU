package com.acktos.blu.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.acktos.blu.R;
import com.acktos.blu.android.InternalStorage;
import com.acktos.blu.presentation.MapTrackerActivity;
import com.acktos.blu.util.MapTrackerUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MapTrackerService extends Service implements
		LocationListener,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {


	private static final String TAG = "TrackServiceDebug";

	//Utils
	InternalStorage storage; 

	//attributes
	String serviceId;
	Location prevLocation=null;
	Location currentLocation=null;
	float distance=0;

	// request and instance of the location client
	private MapTrackerUtils mapTrackerUtils;
	private LocationRequest mLocationRequest;
	private boolean locationUpdatesEnabled=false;

	//shared preferences
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;


	private static final String DEBUG_TAG="service track";

	//notification ID
	private static final int ONGOING_NOTIFICATION_ID=1;

	//GOOGLE API CLIENT
	private GoogleApiClient mGoogleApiClient;
	/**
	 * Determines if the client is in a resolution state, and
	 * waiting for resolution intent to return.
	 */
	private boolean mIsInResolution;
	/**
	 * Request code for auto Google Play Services error resolution.
	 */
	protected static final int REQUEST_CODE_RESOLUTION = 1;

	//override Service class method
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(DEBUG_TAG, "entry onCreate");
		mapTrackerUtils=new MapTrackerUtils(this);
		storage=new InternalStorage(this);
		mPrefs = getSharedPreferences(MapTrackerUtils.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

		//setup location updates request
		createLocationRequest();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i(DEBUG_TAG, "entry onStartCommand");

		//set service id
		serviceId=mapTrackerUtils.getServiceId();

		Intent notificationIntent = new Intent(this, MapTrackerActivity.class);
		notificationIntent.putExtra(MapTrackerUtils.KEY_SERVICE_ID, serviceId);
		notificationIntent.putExtra(MapTrackerUtils.KEY_PICK_UP_ADDRESS, mapTrackerUtils.getPickUpAddress());
		notificationIntent.putExtra(MapTrackerUtils.KEY_FROM_BACKGROUND, true);


		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_map_tracker)
		.setContentTitle(getText(R.string.tag_tracking_in_progress))
		.setContentText(getText(R.string.msg_tracking_in_progress))
		.setContentIntent(pendingIntent);

		startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());

		locationUpdatesEnabled=true;
		buildGoogleApiClient();
		mGoogleApiClient.connect();

		return (START_NOT_STICKY);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mGoogleApiClient.isConnected()) {
			stopLocationUpdates();
		}

		// After disconnect() is called, the client is considered "dead".
		mGoogleApiClient.disconnect();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected synchronized void buildGoogleApiClient() {

		Log.i(TAG,"Entry to buildGoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	protected void createLocationRequest() {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(MapTrackerUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(MapTrackerUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	//override LocationListener methods
	@Override
	public void onLocationChanged(Location location) {

		Log.i(DEBUG_TAG, "location:" + location.toString());
		if(mapTrackerUtils.checkSensors(location)){

			if(currentLocation==null){
				
				if(mPrefs.contains(MapTrackerUtils.KEY_END_LOCATION)){
					try{
						String coordinates=mPrefs.getString(MapTrackerUtils.KEY_END_LOCATION,"");
						currentLocation=MapTrackerUtils.locationFromString(coordinates);
					}catch(ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
					}

				}
			}

			prevLocation=currentLocation;
			currentLocation=location;

			saveLocation(location);

			if(prevLocation!=null && currentLocation!=null){

				distance=mapTrackerUtils.getDistance();
				distance+=MapTrackerUtils.calculateDistanceBetween(prevLocation, currentLocation);
				mapTrackerUtils.saveDistance(distance);
			}
		}
	}

	private boolean saveLocation(Location location){

		String oldCoordinates="";
		String coordinates=location.getLatitude()+","+location.getLongitude();

		if(storage.isFileExists(MapTrackerUtils.FILE_TRACK+"_"+serviceId)){
			oldCoordinates=storage.readFile(MapTrackerUtils.FILE_TRACK+"_"+serviceId);
		}
		
		if(!TextUtils.isEmpty(oldCoordinates)){
			oldCoordinates=oldCoordinates+";";
		}

		if(storage.saveFile(MapTrackerUtils.FILE_TRACK + "_" + serviceId, oldCoordinates + coordinates)){

			Log.i(TAG,"Save location in background");
			mapTrackerUtils.saveEndLocation(location);
			return true;
		}else{
			return false;
		}
	}

	//override ConnectionCallbacks methods
	@Override
	public void onConnected(Bundle arg0) {
		Log.i(DEBUG_TAG, "location connected");
		if (locationUpdatesEnabled) {
			startLocationUpdates();
		}
	}

	/**
	 * Called when {@code mGoogleApiClient} connection is suspended.
	 */
	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(TAG, "GoogleApiClient connection suspended");
		retryConnecting();
	}


	/**
	 * Called when {@code mGoogleApiClient} is trying to connect but failed.
	 * Handle {@code result.getResolution()} if there is a resolution
	 * available.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {

		Log.i(TAG, "GoogleApiClient connection Failed");
	}

	private void retryConnecting() {
		mIsInResolution = false;
		if (!mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();
		}
	}


}
