package com.t.araya.parka.Controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.t.araya.parka.Model.CsvReader;
import com.t.araya.parka.Model.CsvWriter;
import com.t.araya.parka.R;
import com.t.araya.parka.View.AccelerometerDataViewGroup;
import com.t.araya.parka.View.StartStopButtonViewGroup;
import com.t.araya.parka.View.StopEngineButtonViewGroup;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvAccelerometerDataActivity extends AppCompatActivity implements View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor accelSensor;
    private AccelerometerDataViewGroup accelerometerDataViewGroup;
    private StartStopButtonViewGroup startStopButtonViewGroup;
    private StopEngineButtonViewGroup stopEngineButtonViewGroup;
    private DecimalFormat dcm = new DecimalFormat("0.000000");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
    private long startTime;
    private String fileName;
    private CsvWriter csvWriter;
    private boolean isStopEngine;
    private long timeStampAcce = 0, milliSecAcce = 0;
    private int listenerSampling = -1;

    private CsvReader csvReader;
    private boolean isReadFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_sensors);

        initInstances();

        startStopButtonViewGroup.getBtnStart().setOnClickListener(this);
        startStopButtonViewGroup.getBtnStop().setOnClickListener(this);
        accelerometerDataViewGroup.getBtnEnter().setOnClickListener(this);
        stopEngineButtonViewGroup.getBtnStopEngine().setOnClickListener(this);

        Toast.makeText(this, "You can set listener sampling rate", Toast.LENGTH_SHORT).show();
    }

    public void initInstances() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerDataViewGroup = (AccelerometerDataViewGroup) findViewById(R.id.accelerometerDataViewGroup);
        startStopButtonViewGroup = (StartStopButtonViewGroup) findViewById(R.id.startStopButtonViewGroup);
        stopEngineButtonViewGroup = (StopEngineButtonViewGroup) findViewById(R.id.stopEngineButtonViewGroup);

        csvWriter = new CsvWriter();
        csvReader = new CsvReader();

        isStopEngine = false;
        isReadFinish = false;
        fileName = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterListener();
        if(csvWriter.getFile() != null){
            stopRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterListener();
        if(csvWriter.getFile() != null){
            stopRecording();
        }
    }

    private boolean registerListener() {
        // Register sensor listeners
        boolean isSuccess = false;

        if (listenerSampling == -1) {
            listenerSampling = SensorManager.SENSOR_DELAY_NORMAL;
        } else {
            isSuccess = true;
        }
        sensorManager.registerListener(accelListener, accelSensor, listenerSampling);

        return isSuccess;
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(accelListener);
    }

    private SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent eventAcce) {
            float acc_x = eventAcce.values[0];
            float acc_y = eventAcce.values[1];
            float acc_z = eventAcce.values[2];

            milliSecAcce = System.currentTimeMillis() - startTime;
            timeStampAcce = eventAcce.timestamp;

            accelerometerDataViewGroup.setTvAccel_x_text("X : " + dcm.format(acc_x));
            accelerometerDataViewGroup.setTvAccel_y_text("Y : " + dcm.format(acc_y));
            accelerometerDataViewGroup.setTvAccel_z_text("Z : " + dcm.format(acc_z));

//            Log.i("Write Sensor Data", "AcceData: (" + milliSecAcce + ") [" + timeStampAcce + "] x=" + acc_x + " ,y=" + acc_y + " ,z=" + acc_z);
            String line = milliSecAcce + "," + timeStampAcce + ","
                    + dcm.format(acc_x) + "," + dcm.format(acc_y) + "," + dcm.format(acc_z) + ","
                    + isStopEngine +  ",\n";

            if(csvWriter.getFile() != null) {
                csvWriter.writeToFile(line);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onClick(View v) {
        if (v == startStopButtonViewGroup.getBtnStart()) {
            startRecording();
            Toast.makeText(this, "Start recording\n with listener sampling " + listenerSampling, Toast.LENGTH_SHORT).show();
        }

        if (v == startStopButtonViewGroup.getBtnStop()) {
            stopRecording();
            Toast.makeText(this, "Stop recording", Toast.LENGTH_SHORT).show();
        }

        if (v == accelerometerDataViewGroup.getBtnEnter()) {
            /** set unregisterListener brfore set new listenerSampling
                then register registerListener again with new listenerSampling **/

            unregisterListener();

            String listenerSamplingStr = accelerometerDataViewGroup.getEditTextListenerSampling().getText().toString();
            listenerSampling = Integer.parseInt(listenerSamplingStr);

            boolean isSuccess = registerListener();
            accelerometerDataViewGroup.setEditTextListenerSampling(listenerSamplingStr);

            if (isSuccess) {
                Toast.makeText(this, "Listener sampling rate = " + listenerSampling, Toast.LENGTH_SHORT).show();
            }
        }

        if (v == stopEngineButtonViewGroup.getBtnStopEngine()){
            isStopEngine = true;
            Toast.makeText(this, "Stop engine",Toast.LENGTH_SHORT).show();
        }
    }

    private void startRecording() {
        // Prepare data of storage

        Date now = new Date(System.currentTimeMillis());
        fileName = "AccelerometerData_" + sdf.format(now) + "_sampling_" + listenerSampling + "microsec_.csv";
        String directory = Environment.getExternalStorageDirectory() + "/_Parka/AccelerometerCsvFile";

        csvWriter.createFile(fileName,directory);
        startTime = csvWriter.getStartTime();

        String headFileStr = "Millisec" + "," + "TimeStamp" + ","
                + "Acce X" + "," + "Acce Y" + "," + "Acce Z" + ","
                + "Stop engine"+",\n";
        csvWriter.writeHeadFile(headFileStr);

        registerListener();
        Toast.makeText(this, "START RECORDING | "
                + "file name = " + fileName, Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        unregisterListener();

        if(csvWriter.getFile() != null) {
            csvWriter.closeFile();
        }

        if (isReadFinish != true){
            List<String[]> rows = new ArrayList<>();
            try {
                if(fileName != null){
                    rows = csvReader.readCSV(fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Log.i("row size", "size = " + rows.size() + "");

            if (rows.size() != 0) {
                int i = 0;
                while(i < rows.size()) {
                    Log.d("read from csv file",
                            String.format("row %s: %s, %s, %s, %s, %s, %s",
                                    i+1, rows.get(i)[0], rows.get(i)[1],rows.get(i)[2], rows.get(i)[3], rows.get(i)[4], rows.get(i)[5]));
                    i++;
                }

                if(i==rows.size()){
                    isReadFinish = true;
                }
            }
        }

    }
}
