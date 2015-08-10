package com.acktos.blu.controller;

import android.util.Log;

import com.acktos.blu.android.AppController;
import com.acktos.blu.android.Encrypt;
import com.acktos.blu.models.Driver;
import com.acktos.blu.models.Tracking;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acktos on 7/17/15.
 */
public class TrackingController extends BaseController{


    public static final String TAG_SEND_TRACKING_REQUEST="SEND_TRACKING_REQUEST";


    public String sendTracking(
            final Tracking tracking,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                BaseController.API.SEND_TRACKING.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                //JSONObject jsonFields=getFieldsObject(response);
                                //Driver driver= new Driver(jsonFields);
                                //saveDriverProfile(driver);

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

                        Log.d(TAG, error.toString());
                        errorListener.onErrorResponse(error);
                    }
                }){
            /*httpPost.setParam(Bill.KEY_ID, bill.id);
		httpPost.setParam(Bill.KEY_DISTANCE, bill.distance);
		httpPost.setParam(Bill.KEY_TIME, bill.time);
		httpPost.setParam(Bill.KEY_END_LOCATION, bill.endLocation);
		httpPost.setParam(Bill.KEY_END_ADDRESS, bill.endAddress);
		httpPost.setParam(Bill.KEY_FILE, bill.file);
		httpPost.setParam(Bill.KEY_SERVICE_ID, bill.serviceId);
		httpPost.setParam(Bill.KEY_FILE_TRACK, bill.trackFile);*/

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put(Tracking.KEY_SERVICE_ID, tracking.serviceId);
                params.put(Tracking.KEY_DISTANCE, tracking.distance);
                params.put(Tracking.KEY_TIME, tracking.time);
                params.put(Tracking.KEY_END_LOCATION, tracking.endLocation);
                params.put(Tracking.KEY_END_ADDRESS, tracking.endAddress);
                params.put(Tracking.KEY_FILE, tracking.file);
                params.put(Tracking.KEY_FILE_TRACK, tracking.trackFile);

                params.put(Driver.KEY_ENCRYPT,
                        Encrypt.md5(tracking.serviceId+
                                    tracking.distance+
                                    tracking.time+
                                    tracking.endLocation+
                                    tracking.endAddress+
                                    tracking.file+
                                    BaseController.TOKEN));

                Log.i(TAG,"send track"+tracking.distance+"*"+
                        tracking.time+"*"+
                        tracking.endLocation+"*"+
                        tracking.endAddress+"*"+
                        tracking.file+"*"+
                        tracking.serviceId+"*"+
                        BaseController.TOKEN);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_SEND_TRACKING_REQUEST);
        return null;
    }
}
