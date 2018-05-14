package com.yasir_mahareth.weatherapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataClass {

    private String city;
    private int temp;
    private String iconName;
    private int condition;
    private String country;


    public WeatherDataClass(JSONObject jsonObject){
        try{

            this.temp=(int)(jsonObject.getJSONObject("main").getDouble("temp")-273.15);
            this.city=jsonObject.getString("name");
            this.condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            this.country=jsonObject.getJSONObject("sys").getString("country");

        }
        catch (JSONException e){
            Log.d("Clima", "Json Exception: "+e.toString());
            e.printStackTrace();

        }

    }

    public String getCity() {
        return city;
    }

    public int getTemp() {
        return temp;
    }

    public String getIconName() {
        return iconName;
    }

    public String getCountry() {
        return country;
    }

    public int getCondition() {
        return condition;
    }

}

