package com.acktos.blu.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/15/15.
 */
public class Service {

    public String id;
    public String username;
    public String pickupAddress;
    public String arrivalAddress;
    public String price;
    public String date;
    public String zone;
    public String payment;
    public String state;

    public static final String KEY_ID="id";
    public static final String KEY_USERNAME="name";
    public static final String KEY_PICKUP_ADDRESS="place";
    public static final String KEY_ARRIVAL_ADDRESS="destination";
    public static final String KEY_PRICE="rate_value";
    public static final String KEY_DATE="date";
    public static final String KEY_ZONE="rate";
    public static final String KEY_PAYMENT="payment";
    public static final String KEY_STATE="state_name";
    public static final String KEY_JSON="service_JSON";


    public Service(String username,String pickupAddress,String arrivalAddress,String price,String date,
                   String zone,String payment,String state){

        this.username=username;
        this.pickupAddress=pickupAddress;
        this.arrivalAddress=arrivalAddress;
        this.price=price;
        this.date=date;
        this.zone=zone;
        this.payment=payment;
        this.state=state;
    }

    public Service (JSONObject jsonObject){

        try{

            if(jsonObject.has(KEY_ID)) {
                this.id = jsonObject.getString(KEY_ID);
            }

            if(jsonObject.has(KEY_USERNAME)) {
                this.username = jsonObject.getString(KEY_USERNAME);
            }
            if(jsonObject.has(KEY_ARRIVAL_ADDRESS)) {
                this.arrivalAddress = jsonObject.getString(KEY_ARRIVAL_ADDRESS);
            }
            if(jsonObject.has(KEY_PICKUP_ADDRESS)) {
                this.pickupAddress = jsonObject.getString(KEY_PICKUP_ADDRESS);
            }
            if(jsonObject.has(KEY_PRICE)) {
                this.price = jsonObject.getString(KEY_PRICE);
            }
            if(jsonObject.has(KEY_DATE)) {
                this.date = jsonObject.getString(KEY_DATE);
            }
            if(jsonObject.has(KEY_ZONE)) {
                this.zone = jsonObject.getString(KEY_ZONE);
            }
            if(jsonObject.has(KEY_PAYMENT)) {
                this.payment = jsonObject.getString(KEY_PAYMENT);
            }
            if(jsonObject.has(KEY_STATE)) {
                this.state = jsonObject.getString(KEY_STATE);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public Service (String jsonServiceString){

        try{

            JSONObject jsonObject=new JSONObject(jsonServiceString);

            if(jsonObject.has(KEY_ID)) {
                this.id = jsonObject.getString(KEY_ID);
            }

            if(jsonObject.has(KEY_USERNAME)) {
                this.username = jsonObject.getString(KEY_USERNAME);
            }
            if(jsonObject.has(KEY_ARRIVAL_ADDRESS)) {
                this.arrivalAddress = jsonObject.getString(KEY_ARRIVAL_ADDRESS);
            }
            if(jsonObject.has(KEY_PICKUP_ADDRESS)) {
                this.pickupAddress = jsonObject.getString(KEY_PICKUP_ADDRESS);
            }
            if(jsonObject.has(KEY_PRICE)) {
                this.price = jsonObject.getString(KEY_PRICE);
            }
            if(jsonObject.has(KEY_DATE)) {
                this.date = jsonObject.getString(KEY_DATE);
            }
            if(jsonObject.has(KEY_ZONE)) {
                this.zone = jsonObject.getString(KEY_ZONE);
            }
            if(jsonObject.has(KEY_PAYMENT)) {
                this.payment = jsonObject.getString(KEY_PAYMENT);
            }
            if(jsonObject.has(KEY_STATE)) {
                this.state = jsonObject.getString(KEY_STATE);
            }

        }catch(JSONException e){
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
            if(username!=null){
                jsonObject.put(KEY_USERNAME,username);
            }
            if(pickupAddress!=null){
                jsonObject.put(KEY_PICKUP_ADDRESS,pickupAddress);
            }
            if(arrivalAddress!=null){
                jsonObject.put(KEY_ARRIVAL_ADDRESS,arrivalAddress);
            }
            if(price!=null){
                jsonObject.put(KEY_PRICE,price);
            }
            if(date!=null){
                jsonObject.put(KEY_DATE,date);
            }
            if(zone!=null){
                jsonObject.put(KEY_ZONE,zone);
            }
            if(payment!=null){
                jsonObject.put(KEY_PAYMENT,payment);
            }
            if(state!=null){
                jsonObject.put(KEY_DATE,state);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
