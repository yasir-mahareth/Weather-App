package com.yasir_mahareth.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE=123;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    int MIN_TIME=5000;
    int MIN_DISTANCE=1000;
    String Weather_URL="http://api.openweathermap.org/data/2.5/weather";
    String appID="af0e0fc5feac2aed5265470d0a0aa189";
    LocationManager locationManager;
    LocationListener locationListener;
    TextView displayLocation;
    TextView displayTemp;
    ImageView displayIcon;
    ImageButton toChangeCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayLocation = (TextView)findViewById(R.id.txtCity);
        displayTemp = (TextView)findViewById(R.id.txtTemp);
        displayIcon= (ImageView)findViewById(R.id.weatherIcon);
        toChangeCity=(ImageButton)findViewById(R.id.changeCity);

        toChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChangeCity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        Log.d("Clima", "onResume called ");
        Log.d("Clima", "Getting weather for current location");

        Intent intent =getIntent();
        String newCity = intent.getStringExtra("city");
        if(newCity != null){
            getWeatherForCity(newCity);
        }
        else{
            getWeatherForCurrentLocation();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Clima", "onRequestPermissionsResult: Permission granted! ");
                getWeatherForCurrentLocation();
            }
            else{
                Log.d("Clima", "onRequestPermissionsResult: permission denied.");
            }
        }
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged. callback received ");
                String latitude=String.valueOf(location.getLatitude());
                String longitude=String.valueOf(location.getLongitude());

                Log.d("Clima", "onLocationChanged: Latitude is "+latitude);
                Log.d("Clima", "onLocationChanged: longitude is "+longitude);

                RequestParams params= new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
               params.put("appid",appID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Clima", "onProviderEnabled. callback received");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Clima", "onProviderDisabled. callback received ");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
    }
    public void letsDoSomeNetworking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weather_URL,params,new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("Clima", "onSuccess: "+ response.toString());

                displayFetchedDetails(response);

            }
            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                Log.d("Clima", "onFailure: ");
                Log.d("Clima", "Status code: "+statusCode);
                Log.e("Clima",e.toString() );
                Toast.makeText(MainActivity.this,"Request Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void displayFetchedDetails(JSONObject response){
        WeatherDataClass weatherObject= new WeatherDataClass(response);
        displayLocation.setText(weatherObject.getCity()+", "+weatherObject.getCountry());
        displayTemp.setText(weatherObject.getTemp()+"°C");
        int resourdeID= getResources().getIdentifier(weatherObject.getIconName(),"drawable",getPackageName());
        displayIcon.setImageResource(resourdeID);
    }
    public void getWeatherForCity(String city){
        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid",appID);

        letsDoSomeNetworking(params);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
