package com.acktos.blu.controller;

import android.content.Context;
import android.util.Log;

import com.acktos.blu.android.AppController;
import com.acktos.blu.android.Encrypt;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.acktos.blu.models.Driver;
import com.acktos.blu.models.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Acktos on 7/15/15.
 */
public class ServicesController extends BaseController {


    private Context context;
    public final static String TAG_DRIVER_LOGIN_REQUEST="DRIVER_LOGIN_REQUEST";

    public ServicesController(Context context){

        this.context=context;
    }

    public void getAvailableServices(
            final String driverId,
            final Response.Listener<List<Service>> responseListener,
            final Response.ErrorListener errorListener) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                API.AVAILABLE_SERVICES.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());
                        List<Service> services;
                        String code = getCodeRequest(response);
                        if (code != null) {
                            if (code.equals(SUCCESS_CODE)) {

                                services = new ArrayList<Service>();
                                JSONArray jsonArrayServices = getFieldsArray(response);

                                if (jsonArrayServices != null) {

                                    for (int i = 0; i < jsonArrayServices.length(); i++) {

                                        try {
                                            JSONObject serviceObject = jsonArrayServices.getJSONObject(i);
                                            services.add(new Service(serviceObject));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    responseListener.onResponse(services);
                                }

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                errorListener.onErrorResponse(volleyError);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Driver.KEY_ID, driverId);
                params.put(Driver.KEY_ENCRYPT, Encrypt.md5(driverId + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_DRIVER_LOGIN_REQUEST);

    }

}
