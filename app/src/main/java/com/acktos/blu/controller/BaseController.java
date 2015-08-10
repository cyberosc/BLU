package com.acktos.blu.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/13/15.
 */
public class BaseController {


    // APP CONSTANTS
    public static final String TAG="com.acktos.blue.debug";
    public static final String TOKEN="ee8099de39d5167fe135baf92fa0df1c";


    /**
     * API RESTfull endpoints
     */
    public enum API{

        DRIVER_LOGIN("http://www.blue.acktos.com.co/login_driver/"),
        AVAILABLE_SERVICES("http://www.blue.acktos.com.co/find_driver_services/"),
        SEND_TRACKING("http://www.blue.acktos.com.co/add_bill/"),
        SAVE_REGISTER_ID("http://www.blue.acktos.com.co/edit_mobile_driver/"),
        TEST_POST("http://www.blue.acktos.com.co/print_post/");

        private final String url;

        API (String uri){
            url=uri;
        }

        public String getUrl(){
            return url;
        }

    }

    // API RESTfull CONSTANTS
    public static final String KEY_FIELDS="fields";
    public static final String KEY_CODE="response";
    public static final String SUCCESS_CODE="200";
    public static final String FAILED_CODE="402";
    public static final String ERROR_CODE="400";


    public static JSONObject getFieldsObject(String jsonString){

        JSONObject jsonFields=null;

        try{

            JSONObject jsonObject= new JSONObject(jsonString);
            jsonFields=jsonObject.getJSONObject(KEY_FIELDS);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonFields;
    }

    public static JSONArray getFieldsArray(String jsonString){

        JSONArray jsonArray=null;

        try{

            JSONObject jsonObject= new JSONObject(jsonString);
            jsonArray=jsonObject.getJSONArray(KEY_FIELDS);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static String getCodeRequest(String jsonString){

        String code=null;

        try{
            JSONObject jsonObject=new JSONObject(jsonString);
            code=jsonObject.getString(KEY_CODE);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return code;

    }
}
