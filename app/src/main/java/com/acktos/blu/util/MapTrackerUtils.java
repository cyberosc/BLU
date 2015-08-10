package com.acktos.blu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.acktos.blu.android.InternalStorage;
import com.google.android.gms.maps.model.LatLng;


public class MapTrackerUtils {

	Context context;

	//Utils
	InternalStorage storage;

	//shared preferences
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;


    //Accuracy values
    public static final float MIN_ACCURACY=12.0f;
    public static final float MAX_ACCURACY=16.0f;
    public static final float STOP_SPEED=0.5f;

	// keys for save bill data in shared preferences
    public static final String KEY_SHARED_PREFERENCES="MAP_TRACKING_PREFERENCES";
	public static final String KEY_DISTANCE="DISTANCE";
	public static final String KEY_START_TIME="START_TIME";
	public static final String KEY_END_LOCATION="END_LOCATION";
	public static final String KEY_STATE_TRACKING="STATE_TRACKING";
    public static final String KEY_SERVICE_ID="SERVICE_ID";
    public static final String KEY_PICK_UP_ADDRESS="SERVICE_ID";
    public static final String KEY_ARRIVAL_ADRESS="SERVICE_ID";
    public static final String KEY_FROM_BACKGROUND="com.acktos.conductorvip.FROM_BACKGROUND";


	//this file saves coordinates from a single service
	public static final String FILE_TRACK="com.acktos.conductorvip.TRACK_SERVICE";

	//this file saves failed services IDs
	public static final String FILE_FAILED_BILLS="com.acktos.conductorvip.FAILED_BILLS";

	//this file saves info of file service temporally
	public static final String FILE_BILLS="com.acktos.conductorvip.BILL";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED ="com.acktos.conductorvip.KEY_UPDATES_REQUESTED";

    //key for storing accumulated distance
    public static final String KEY_ACCUMULATED_DISTANCE="com.acktos.conductorvip.KEY_ACCUMULATED_DISTANCE";

    //key for save current zoom map
    public static final String KEY_CURRENT_ZOOM="com.acktos.conductorvip.KEY_CURRENT_ZOOM";



    //CONSTANTS FOR LOCATION UPDATE PARAMETERS

    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // Meters  per kilometer
    public static final int METERS_PER_KILOMETER = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 5;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();

	//tracking states
	public static final int STARTED=72;
	public static final int COMPLETED=73;
	public static final int PENDING_FOR_BILL=74;
	public static final int PAUSED=75;

	// Accuracy sensor
	public static int GOOD_ACCURACY=2;
	public static int EXCELENT_ACCURACY=1;


    //default zoom
    public static final float DEFAULT_ZOOM=15;

    public static int sumaCoordenadas=0;


	public MapTrackerUtils(Context context){

		this.context=context;

		storage=new InternalStorage(context);
		mPrefs = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
	}

	public void saveDistance(float distance){
		mEditor.putFloat(KEY_DISTANCE, distance);
		mEditor.commit();
	}

	public void saveStartTime(long timestamp){
		mEditor.putLong(KEY_START_TIME, timestamp);
		mEditor.commit();
	}

	public void saveEndLocation(Location location){

		if(location!=null){
			mEditor.putString(KEY_END_LOCATION, location.getLatitude()+","+location.getLongitude());
			mEditor.commit();
		}else{
			Log.e("saveEndLocation error","end location is null");
		}
	}

	public void saveServiceId(String serviceId){

		if(serviceId!=null){
			mEditor.putString(KEY_SERVICE_ID, serviceId);
			mEditor.commit();

			Log.i(KEY_SERVICE_ID,serviceId);
		}else{
			Log.e("saveServiceId error","service id is null");
		}
	}

	public void savePickUpAddress(String address){

		if(address!=null){
			mEditor.putString(KEY_PICK_UP_ADDRESS, address);
			mEditor.commit();
			Log.i(KEY_PICK_UP_ADDRESS,address);
		}else{
			Log.e("savePickUpAddress error","address is null");
		}
	}

	public void saveStateTracking(int state){

		mEditor.putInt(KEY_STATE_TRACKING, state);
		mEditor.commit();
	}

	public String getServiceId(){

		return  mPrefs.getString(KEY_SERVICE_ID,"");
	}

	public String getPickUpAddress(){

		return mPrefs.getString(KEY_PICK_UP_ADDRESS,"");

	}

	public Float getDistance(){

		return  mPrefs.getFloat(KEY_DISTANCE,0);
	}

	public Long getStartTime(){

		return  mPrefs.getLong(KEY_START_TIME, 0);
	}

	public String getEndLocation(){

		return  mPrefs.getString(KEY_END_LOCATION,"");
	}

	public int getStateTracking(){
		return  mPrefs.getInt(KEY_STATE_TRACKING, 0);
	}

	public boolean isFileTrackEmpty(String serviceId){

		boolean success =true;
		if(storage.isFileExists(FILE_TRACK+"_"+serviceId)){

			String coordinates=storage.readFile(FILE_TRACK+"_"+serviceId);
			if(!TextUtils.isEmpty(coordinates)){
				success=false;
			}
		}
		return success;
	}

	public boolean checkSensors(Location location){

		float accuracy=location.getAccuracy();
		float speed=location.getSpeed();

		if(accuracy<=MIN_ACCURACY && speed>=STOP_SPEED){
		//if(accuracy<=LocationUtils.MIN_ACCURACY ){
			return true;
		}else{
			return false;
		}

	}

	public static float calculateDistanceBetween(Location startPoint, Location endPoint){

		float distance =0;

		Location startLocation=new Location("");
		Location endLocation=new Location("");

		startLocation.set(startPoint);
		endLocation.set(endPoint);

		distance=startLocation.distanceTo(endLocation);

		return distance;
	}
	
	public static Location locationFromString(String coordinates){
		
		Location location=new Location("");
		try{
			
			double lat= Double.parseDouble(coordinates.split(",")[0]);
			double lng= Double.parseDouble(coordinates.split(",")[1]);
			location=new Location("");
			location.setLatitude(lat);
			location.setLongitude(lng);
			
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		
		return location;
	}

	public static LatLng getLatLng(Location location){

		if(location !=null){
			return new LatLng(location.getLatitude(),location.getLongitude());
		}else{
			return null;
		}

	}

	public static float getSpeed(float speed){

		int speedKm;
		speedKm=(int) (speed*3600)/1000;
		return speedKm;
	}

	public static float getDistanceKm(float distance){
		try{
			//Log.i("result kilometers",(float) Math.rint((distance/METERS_PER_KILOMETER)*100)/100 +"");
			return (float) Math.rint((distance/1000)*100)/100;

		}catch(ArithmeticException e){
			return 0;

		}

	}

}
