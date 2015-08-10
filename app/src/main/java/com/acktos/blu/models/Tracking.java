package com.acktos.blu.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/17/15.
 */
public class Tracking {

    public String id;
    public String distance;
    public String time;
    public String endLocation;
    public String endAddress;
    public String file;
    public String serviceId;
    public String trackFile;

    public static final String KEY_ID="id";
    public static final String KEY_DISTANCE="distance";
    public static final String KEY_TIME="time";
    public static final String KEY_END_LOCATION="end_location";
    public static final String KEY_END_ADDRESS="end_address";
    public static final String KEY_FILE="file";
    public static final String KEY_SERVICE_ID="service_id";
    public static final String KEY_FILE_TRACK="tracking";


    public Tracking (String id,String distance,String time,String endLocation,String endAddress,String file,
                     String serviceId,String trackFile){

        this.id=id;
        this.distance=distance;
        this.time=time;
        this.endLocation=endLocation;
        this.endAddress=endAddress;
        this.file=file;
        this.serviceId=serviceId;
        this.trackFile=trackFile;
    }

    @Override
    public String toString(){

        JSONObject jsonObject=new JSONObject();

        try{
            if(id!=null){
                jsonObject.put(KEY_ID,id);
            }
            if(distance!=null){
                jsonObject.put(KEY_DISTANCE,distance);
            }
            if(time!=null){
                jsonObject.put(KEY_TIME,time);
            }
            if(endLocation!=null){
                jsonObject.put(KEY_END_LOCATION,endLocation);
            }
            if(endAddress!=null){
                jsonObject.put(KEY_END_ADDRESS,endAddress);
            }
            if(file!=null){
                jsonObject.put(KEY_FILE,file);
            }
            if(serviceId!=null){
                jsonObject.put(KEY_SERVICE_ID,serviceId);
            }
            if(trackFile!=null){
                jsonObject.put(KEY_FILE,trackFile);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


}
