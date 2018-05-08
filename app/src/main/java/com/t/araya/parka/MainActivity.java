package com.t.araya.parka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDataFromSensors;
    Button btnDataFromBeacon;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        btnDataFromSensors.setOnClickListener(this);
        btnDataFromBeacon.setOnClickListener(this);

    }

    public void initInstances() {

        btnDataFromSensors = (Button) findViewById(R.id.btnDataFromSensors);
        btnDataFromBeacon = (Button) findViewById(R.id.btnDataFromBeacon);

    }


    @Override
    public void onClick(View v) {

        if (v == btnDataFromSensors) {
            Toast.makeText(this, "in btn data from sensors", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, DataFromSensorsActivity.class);
            startActivity(intent);

        }

        if (v == btnDataFromBeacon) {
            Toast.makeText(this, "in btn data from beacon", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, DataFromBeaconActivity.class);
            startActivity(intent);

        }

    }
}
