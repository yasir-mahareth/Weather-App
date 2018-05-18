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
    private String description;


    public WeatherDataClass(JSONObject jsonObject){
        try{

            this.temp=(int)(jsonObject.getJSONObject("main").getDouble("temp")-273.15);
            this.city=jsonObject.getString("name");
            this.country=jsonObject.getJSONObject("sys").getString("country");
            this.condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            this.iconName=updateWeatherIcon(condition);
            this.description=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

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

    public String getDescription() {
        return description;
    }

    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }
}

