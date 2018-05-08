package com.t.araya.parka;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.t.araya.parka.View.SensorsViewGroup;
import java.text.DecimalFormat;

public class DataFromSensorsActivity extends AppCompatActivity {

    TextView tvAccel_x, tvAccel_y, tvAccel_z, tvGyro_x, tvGyro_y, tvGyro_z;
    SensorManager sensorManager;
    Sensor accelSensor, gyroSensor;
    DecimalFormat dcm = new DecimalFormat("0.0000");
    SensorsViewGroup sensorsViewGroup;

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
//        tvAccel_x = findViewById(R.id.tvAccel_x);
//        tvAccel_y = findViewById(R.id.tvAccel_y);
//        tvAccel_z = findViewById(R.id.tvAccel_z);
//
//        tvGyro_x = findViewById(R.id.tvGyro_x);
//        tvGyro_y = findViewById(R.id.tvGyro_y);
//        tvGyro_z = findViewById(R.id.tvGyro_z);

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

            sensorsViewGroup.setTvAccel_x_text("X : " + dcm.format(acc_x));
            sensorsViewGroup.setTvAccel_y_text("Y : " + dcm.format(acc_y));
            sensorsViewGroup.setTvAccel_z_text("Z : " + dcm.format(acc_z));

//            tvAccel_x.setText("X : " + dcm.format(acc_x));
//            tvAccel_y.setText("Y : " + dcm.format(acc_y));
//            tvAccel_z.setText("Z : " + dcm.format(acc_z));

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

            sensorsViewGroup.setTvGyro_x_text("X : " + dcm.format(gy_x));
            sensorsViewGroup.setTvGyro_y_text("Y : " + dcm.format(gy_y));
            sensorsViewGroup.setTvGyro_z_text("Z : " + dcm.format(gy_z));

//            tvGyro_x.setText("X : " + dcm.format(gy_x));
//            tvGyro_y.setText("Y : " + dcm.format(gy_y));
//            tvGyro_z.setText("Z : " + dcm.format(gy_z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
