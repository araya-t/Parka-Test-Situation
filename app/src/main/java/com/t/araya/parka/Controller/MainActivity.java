package com.t.araya.parka.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.t.araya.parka.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCsvSensors;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        btnCsvSensors.setOnClickListener(this);
    }

    public void initInstances() {

        btnCsvSensors = (Button) findViewById(R.id.btnCsvSensors);

    }


    @Override
    public void onClick(View v) {

        if (v == btnCsvSensors){
            Intent intent = new Intent(this, CsvAccelerometerDataActivity.class);
            startActivity(intent);
        }

    }
}
