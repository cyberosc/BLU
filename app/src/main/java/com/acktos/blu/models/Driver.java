package com.acktos.blu.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/13/15.
 */
public class Driver {

    public String id;
    public String name;
    public String plate;
    public String cc;
    public String brand;
    public String phone;

    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_PLATE="plate";
    public static final String KEY_CC="cc";
    public static final String KEY_BRAND="brand";
    public static final String KEY_PHONE="phone";


    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="pswrd";
    public static final String KEY_ENCRYPT="encrypt";

    public Driver(String id, String name,String plate,String cc,String brand,String phone){

        this.id=id;
        this.name=name;
        this.plate=plate;
        this.cc=cc;
        this.brand=brand;
        this.phone=phone;
    }

    public Driver (JSONObject jsonObject){

        try{
            if(jsonObject.has(KEY_ID)) {
                 this.id = jsonObject.getString(KEY_ID);
            }
            if(jsonObject.has(KEY_NAME)) {
                this.name = jsonObject.getString(KEY_NAME);
            }
            if(jsonObject.has(KEY_PLATE)) {
                this.plate = jsonObject.getString(KEY_PLATE);
            }
            if(jsonObject.has(KEY_CC)) {
                this.cc = jsonObject.getString(KEY_CC);
            }
            if(jsonObject.has(KEY_BRAND)) {
                this.brand = jsonObject.getString(KEY_BRAND);
            }
            if(jsonObject.has(KEY_PHONE)) {
                this.phone = jsonObject.getString(KEY_PHONE);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public String toString(){

        JSONObject jsonObject=new JSONObject();

        try{
            if(id!=null){
                jsonObject.put(KEY_ID,id);
            }
            if(name!=null){
                jsonObject.put(KEY_NAME,name);
            }
            if(plate!=null){
                jsonObject.put(KEY_PLATE,plate);
            }
            if(cc!=null){
                jsonObject.put(KEY_CC,cc);
            }
            if(brand!=null){
                jsonObject.put(KEY_BRAND,brand);
            }
            if(phone!=null){
                jsonObject.put(KEY_PHONE,phone);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
