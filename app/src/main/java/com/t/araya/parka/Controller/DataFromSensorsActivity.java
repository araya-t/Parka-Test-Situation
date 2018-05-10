package com.t.araya.parka.Controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.t.araya.parka.R;
import com.t.araya.parka.View.SensorsViewGroup;

import java.text.DecimalFormat;

public class DataFromSensorsActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor accelSensor, gyroSensor;
    DecimalFormat dcm = new DecimalFormat("0.0000");
    SensorsViewGroup sensorsViewGroup;
    String line = "";
    long start = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_from_sensors);

        initInstances();

    }

    public void initInstances() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorsViewGroup = (SensorsViewGroup) findViewById(R.id.sensorsViewGroup);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelListener);
        sensorManager.unregisterListener(gyroListener);
    }

    SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent eventAcc) {
            float acc_x = eventAcc.values[0];
            float acc_y = eventAcc.values[1];
            float acc_z = eventAcc.values[2];
            long timeStamp = eventAcc.timestamp;

            sensorsViewGroup.setTvAccel_x_text("X : " + dcm.format(acc_x));
            sensorsViewGroup.setTvAccel_y_text("Y : " + dcm.format(acc_y));
            sensorsViewGroup.setTvAccel_z_text("Z : " + dcm.format(acc_z));

            String line = "Accel: [Time:"+ (System.currentTimeMillis() - start)+"] [TS:" + eventAcc.timestamp + "] ( x = "+ dcm.format(acc_x) +",y = "+ dcm.format(acc_y) +",z = "+ dcm.format(acc_z)+" )";
            System.out.println(line);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent eventGyro) {
            float gy_x = eventGyro.values[0];
            float gy_y = eventGyro.values[1];
            float gy_z = eventGyro.values[2];
            long timeStamp = eventGyro.timestamp;

            sensorsViewGroup.setTvGyro_x_text("X : " + dcm.format(gy_x));
            sensorsViewGroup.setTvGyro_y_text("Y : " + dcm.format(gy_y));
            sensorsViewGroup.setTvGyro_z_text("Z : " + dcm.format(gy_z));

            String line = "Gyro: [Time:"+ (System.currentTimeMillis() - start) +"] [TS:" + eventGyro.timestamp + "] ( x = "+ dcm.format(gy_x) +",y = "+ dcm.format(gy_y) +",z = "+ dcm.format(gy_z)+" )";
            System.out.println(line);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };





}
