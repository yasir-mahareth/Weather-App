package com.yasir_mahareth.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCity extends AppCompatActivity {

    EditText cityInput;
    ImageButton btnToMain;
    String newCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        cityInput = (EditText)findViewById(R.id.text_input_city);
        btnToMain = (ImageButton)findViewById(R.id.changeToMain);





        btnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity= cityInput.getText().toString();
                Intent newCityIntent = new Intent(ChangeCity.this,MainActivity.class);
                newCityIntent.putExtra("city",newCity);
                startActivity(newCityIntent);
                return false;
            }
        });
    }
}
