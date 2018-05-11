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
import android.widget.Button;
import android.widget.Toast;

import com.t.araya.parka.R;
import com.t.araya.parka.View.SensorsViewGroup;
import com.t.araya.parka.View.StartStopButtonViewGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CsvSensorsActivity extends AppCompatActivity implements View.OnClickListener {

    SensorManager sensorManager;
    Sensor accelSensor, gyroSensor;
    SensorsViewGroup sensorsViewGroup;
    StartStopButtonViewGroup startStopButtonViewGroup;
    DecimalFormat dcm = new DecimalFormat("0.0000");
    String line = "";
    long startTime;
    private BufferedWriter file;
    float acc_x = 0, acc_y = 0, acc_z = 0, gy_x = 0, gy_y = 0, gy_z = 0;
    long timeStampAcce = 0, timeStampGyro = 0, milliSecAcce = 0, milliSecGyro = 0;

//    private Map<Integer, String> sensorTypes = new HashMap<Integer, String>();
//    private Map<Integer, Sensor> sensors = new HashMap<Integer, Sensor>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_sensors);

        initInstances();

        startStopButtonViewGroup.getBtnStart().setOnClickListener(this);
        startStopButtonViewGroup.getBtnStop().setOnClickListener(this);

    }

    public void initInstances() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorsViewGroup = (SensorsViewGroup) findViewById(R.id.sensorsViewGroup);
        startStopButtonViewGroup = (StartStopButtonViewGroup) findViewById(R.id.startStopButtonViewGroup);

//        sensorTypes.put(Sensor.TYPE_ACCELEROMETER, "ACCEL");
//        sensorTypes.put(Sensor.TYPE_GYROSCOPE, "GYRO");
//
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        for (Integer type : sensorTypes.keySet()) {
//            sensors.put(type, sensorManager.getDefaultSensor(type));
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
//        for (Sensor sensor : sensors.values()) {
//            sensorManager.registerListener(sensorListener, sensor, 500);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterListener();
        stopRecording();
    }

    private void registerListener() {
        // Register sensor listeners
//        sensorManager.registerListener(sensorListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(sensorListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void unregisterListener() {
//        sensorManager.unregisterListener(sensorListener);

        sensorManager.unregisterListener(accelListener);
        sensorManager.unregisterListener(gyroListener);
    }

    SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent eventGyro) {
            float gy_x = eventGyro.values[0];
            float gy_y = eventGyro.values[1];
            float gy_z = eventGyro.values[2];
            milliSecGyro = System.currentTimeMillis() - startTime;
            timeStampGyro = eventGyro.timestamp;

            sensorsViewGroup.setTvGyro_x_text("X : " + dcm.format(gy_x));
            sensorsViewGroup.setTvGyro_y_text("Y : " + dcm.format(gy_y));
            sensorsViewGroup.setTvGyro_z_text("Z : " + dcm.format(gy_z));

            Log.i("Sensor Data", "GyroData: (" + milliSecGyro + ") [" + timeStampGyro + "] x=" + gy_x + " ,y=" + gy_y + " ,z=" + gy_z);

            String line = milliSecGyro + "," + timeStampGyro + "," + " " + "," + " " + "," + " " + ","
                    + dcm.format(gy_x) + "," + dcm.format(gy_y) + "," + dcm.format(gy_z) + "," + "\n";

            Log.i("Line in writeGyroData", "------------gyroooooooooooo-------- || " + line);
            writeToFile(line);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent eventAcce) {
            float acc_x = eventAcce.values[0];
            float acc_y = eventAcce.values[1];
            float acc_z = eventAcce.values[2];

            milliSecAcce = System.currentTimeMillis() - startTime;
            timeStampAcce = eventAcce.timestamp;

            sensorsViewGroup.setTvAccel_x_text("X : " + dcm.format(acc_x));
            sensorsViewGroup.setTvAccel_y_text("Y : " + dcm.format(acc_y));
            sensorsViewGroup.setTvAccel_z_text("Z : " + dcm.format(acc_z));

            Log.i("Sensor Data", "AcceData: (" + milliSecAcce + ") [" + timeStampAcce + "] x=" + acc_x + " ,y=" + acc_y + " ,z=" + acc_z);

            String line = milliSecAcce + "," + timeStampAcce + ","
                    + dcm.format(acc_x) + "," + dcm.format(acc_y) + "," + dcm.format(acc_z) + ","
                    + " " + "," + " " + "," + " " + ",\n";

            Log.i("Line in writeAcceData", "------------accceeeeeeeeeee-----||" + line);
            writeToFile(line);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

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
            Toast.makeText(this, "in start btn", Toast.LENGTH_SHORT).show();
            startRecording();
        }

        if (v == startStopButtonViewGroup.getBtnStop()) {
            Toast.makeText(this, "in stop btn", Toast.LENGTH_SHORT).show();
            stopRecording();
        }
    }

    private void startRecording() {
        // Prepare data storage

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        Date now = new Date(System.currentTimeMillis());
        String name = "SensorsData_" + now + "_" + now.getTime() + ".csv";

        File filename = new File(directory, name);

        try {
            file = new BufferedWriter(new FileWriter(filename));
            startTime = System.currentTimeMillis();
            writeHeadFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        registerListener();
        Toast.makeText(this, "IN START RECORDING | file name = " + filename, Toast.LENGTH_SHORT).show();

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

        String line = "Millisec" + "," + "TimeStamp" + "," + "Acce X" + "," + "Acce Y" + "," + "Acce Z" + ","
                + "Gyro X" + "," + "Gyro Y" + "," + "Gyro Z" + "," + "\n";
        try {
            file.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
