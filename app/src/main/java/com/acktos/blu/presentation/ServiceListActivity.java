package com.acktos.blu.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.acktos.blu.controller.BaseController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.acktos.blu.R;
import com.acktos.blu.controller.DriversController;
import com.acktos.blu.controller.ServicesController;
import com.acktos.blu.models.Driver;
import com.acktos.blu.models.Service;
import com.acktos.blu.presentation.adapters.ServiceAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceListActivity extends AppCompatActivity implements ServiceAdapter.OnRecyclerViewClickListener {

    //ATTRIBUTES
    private List<Service> services;
    private RecyclerView.Adapter recyclerAdapter;

    //APP COMPONENTS
    private ServicesController servicesController;
    private DriversController driversController;

    //UI REFERENCES
    private RecyclerView servicesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;


    //Google Register ID
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENDER_ID = "331183627800";


    String regid;
    GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        //initialize attributes
        services=new ArrayList<>();

        //initialize componentes
        servicesController=new ServicesController(this);
        driversController=new DriversController(this);

        servicesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_services);
        servicesRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        servicesRecyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ServiceAdapter(services,this);
        servicesRecyclerView.setAdapter(recyclerAdapter);

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(BaseController.TAG, "No valid Google Play Services APK found.");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Driver driver=driversController.getDriver();
        Log.i(BaseController.TAG, driver.toString());

        servicesController.getAvailableServices(driver.id, new Response.Listener<List<Service>>() {
            @Override
            public void onResponse(List<Service> responseServices) {

                if(services!=null){

                    services.clear();
                    services.addAll(responseServices);
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(BaseController.TAG, volleyError.toString());
            }
        });

        checkPlayServices();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerViewClick(View v, int position) {

        Intent i=new Intent(this,MapTrackerActivity.class);
        i.putExtra(Service.KEY_JSON,services.get(position).toString());

        startActivity(i);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(BaseController.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     * @return registration ID, or empty string if there is no existing registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        final String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Log.i(BaseController.TAG, "Registration not found.");
            return "";
        }else{
            Log.i(BaseController.TAG,"Registration id:"+registrationId);
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(BaseController.TAG, "App version changed.");
            return "";
        }else{
            new AsyncTask<Void,Void,Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    sendRegistrationIdToBackend(registrationId);
                    return null;
                }

            }.execute(null, null, null);
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        //Persists the registration ID in shared preferences.
        return getSharedPreferences(ServiceListActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ServiceListActivity.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // save registerId in backend
                    sendRegistrationIdToBackend(regid);

                    // save regiterId in shared preferences
                    storeRegistrationId(ServiceListActivity.this, regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(BaseController.TAG,msg);
            }

        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String registerId) {


        driversController.saveRegisterId(registerId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(BaseController.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
