package com.t.araya.parka.Controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.t.araya.parka.R;
import com.t.araya.parka.View.AccelerometerDataViewGroup;
import com.t.araya.parka.View.StartStopButtonViewGroup;
import com.t.araya.parka.View.StopEngineButtonViewGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvAccelerometerDataActivity extends AppCompatActivity implements View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor accelSensor;
    private AccelerometerDataViewGroup accelerometerDataViewGroup;
    private StartStopButtonViewGroup startStopButtonViewGroup;
    private StopEngineButtonViewGroup stopEngineButtonViewGroup;
    private DecimalFormat dcm = new DecimalFormat("0.000000");
    private long startTime;
    private BufferedWriter file = null;
    private long timeStampAcce = 0, milliSecAcce = 0;
    private int listenerSampling = -1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
    private boolean isStopEngine = false;

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
        if(file!=null){
            stopRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterListener();
        if(file!=null){
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

            writeToFile(line);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

//    private void createDirectory(){
//        String myfolder = Environment.getExternalStorageDirectory()+"/Parka";
//        File f = new File(myfolder);
//        if(!f.exists())
//            if(!f.mkdir()){
//                Toast.makeText(this, myfolder + " can't be created.", Toast.LENGTH_SHORT).show();
//
//            }
//            else
//                Toast.makeText(this, myfolder + " can be created.", Toast.LENGTH_SHORT).show();
//    }

    private void writeToFile(String line) {
        if (file == null) {
            return;
        }

        try {
            file.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
        // Prepare data storage
        boolean isMkdirSuccess = false;

        Date now = new Date(System.currentTimeMillis());
        String fileName = "AccelerometerData_" + sdf.format(now) + "_sampling_" + listenerSampling + "microsec_.csv";
        String directory = Environment.getExternalStorageDirectory() + "/_Parka/AccerometerCsvFile";

//        File initFile = new File(directory, fileName);
        File initFile = new File(directory);

        if(!initFile.exists()){
            isMkdirSuccess = initFile.mkdirs();
        }else{
            isMkdirSuccess = true;
        }

        System.out.println("FILE NAME --> "+ fileName);
        System.out.println("DIRECTORY -----> " + directory);
        System.out.println("getAbsolutePath -----> " + initFile.getAbsolutePath());

//        CSVWriter csvw = new CSVWriter(new FileWriter(dir.getAbsolutePath()+"/results.csv"));
        try {
            file = new BufferedWriter(new FileWriter(initFile.getAbsolutePath()+"/"+fileName));
            startTime = System.currentTimeMillis();
            writeHeadFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        registerListener();
        Toast.makeText(this, "START RECORDING | "
                + isMkdirSuccess
                + "file name = " + fileName, Toast.LENGTH_SHORT).show();

    }

    private void stopRecording() {
        unregisterListener();
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeadFile() {
        String line = "Millisec" + "," + "TimeStamp" + ","
                + "Acce X" + "," + "Acce Y" + "," + "Acce Z" + ","
                + "Stop engine"+",\n";
        try {
            file.write(line);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
