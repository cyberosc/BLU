package com.acktos.blu.controller;

import android.content.Context;
import android.util.Log;

import com.acktos.blu.android.AppController;
import com.acktos.blu.android.Encrypt;
import com.acktos.blu.android.HttpRequest;
import com.acktos.blu.android.InternalStorage;
import com.acktos.blu.models.Driver;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acktos on 7/13/15.
 */
public class DriversController extends BaseController {

    private Context context;
    private InternalStorage internalStorage;
    public final static String TAG_DRIVER_LOGIN_REQUEST="10";
    public final static String TAG_TEST_POST="20";
    public final static String FILE_DRIVER_PROFILE="com.acktos.blu.DRIVER_PROFILE";


    public DriversController(Context context){
        this.context=context;
        internalStorage=new InternalStorage(this.context);
    }

    public String driverLogin(
            final String email,
            final String password,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.DRIVER_LOGIN.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                JSONObject jsonFields=getFieldsObject(response);
                                Driver driver= new Driver(jsonFields);
                                saveDriverProfile(driver);
                                responseListener.onResponse(SUCCESS_CODE);
                            } else {
                                responseListener.onResponse(FAILED_CODE);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {

                 @Override
                 public void onErrorResponse(VolleyError error) {

                     errorListener.onErrorResponse(error);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Driver.KEY_EMAIL, email);
                params.put(Driver.KEY_PASSWORD, password);
                params.put(Driver.KEY_ENCRYPT, Encrypt.md5(email + password + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_DRIVER_LOGIN_REQUEST);
        return null;
    }


    /**
     * save profile into internal storage
     */
    private void saveDriverProfile(Driver driver){

        internalStorage.saveFile(FILE_DRIVER_PROFILE, driver.toString());
    }


    public boolean profileExists(){

        boolean exists=false;

        Driver driver=getDriver();

        if(driver!=null){
            exists=true;
        }
        return exists;
    }

    public Driver getDriver(){

        Driver driver=null;

        if(internalStorage.isFileExists(FILE_DRIVER_PROFILE)){

            try {
                String profileString=internalStorage.readFile(FILE_DRIVER_PROFILE);
                JSONObject jsonObject=new JSONObject(profileString);

                driver=new Driver(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return driver;
    }


    public boolean testServerResponse(String username,String password){


        boolean success=true;


        HttpRequest httpPost=new HttpRequest(API.DRIVER_LOGIN.getUrl());

        httpPost.setParam(Driver.KEY_EMAIL, username);
        httpPost.setParam(Driver.KEY_PASSWORD, password);
        httpPost.setParam(Driver.KEY_ENCRYPT, "hello");


        String responseData=httpPost.postRequest();

        Log.i("response test server", responseData);

        return success;
    }

    public String saveRegisterId(

            final String registerId,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.SAVE_REGISTER_ID.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                Log.i(TAG,"Register id saved successful");
                                responseListener.onResponse(SUCCESS_CODE);
                            } else {
                                Log.i(TAG,"Register is NOT saved");
                                responseListener.onResponse(FAILED_CODE);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {

                Driver driver=getDriver();

                Map<String, String> params = new HashMap<>();

                params.put(Driver.KEY_ID, driver.id);
                params.put(Driver.KEY_REGISTER_ID, registerId);
                params.put(Driver.KEY_ENCRYPT, Encrypt.md5(driver.id + registerId + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_DRIVER_LOGIN_REQUEST);
        return null;
    }
}
