package com.t.araya.parka.View;


import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.t.araya.parka.R;

public class SensorsViewGroup extends FrameLayout {

    private TextView tvAccel_x;
    private TextView tvAccel_y;
    private TextView tvAccel_z;
    private TextView tvGyro_x;
    private TextView tvGyro_y;
    private TextView tvGyro_z;

    public SensorsViewGroup(@NonNull Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SensorsViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public SensorsViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
    }

    @TargetApi(21)
    public SensorsViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        // Inflate Layout here
        inflate(getContext(), R.layout.sensors_data_layout, this);

    }

    private void initInstances() {
        // findViewById of item in ViewGroup here

        tvAccel_x = (TextView) findViewById(R.id.tvAccel_x);
        tvAccel_y = (TextView) findViewById(R.id.tvAccel_y);
        tvAccel_z = (TextView) findViewById(R.id.tvAccel_z);

        tvGyro_x = (TextView) findViewById(R.id.tvGyro_x);
        tvGyro_y = (TextView) findViewById(R.id.tvGyro_y);
        tvGyro_z = (TextView) findViewById(R.id.tvGyro_z);

    }

    public void setTvAccel_x_text (String text) {
        tvAccel_x.setText(text);
    }

    public void setTvAccel_y_text (String text) {
        tvAccel_y.setText(text);
    }

    public void setTvAccel_z_text (String text) {
        tvAccel_z.setText(text);
    }

    public void setTvGyro_x_text (String text) {
        tvGyro_x.setText(text);
    }

    public void setTvGyro_y_text (String text) {
        tvGyro_y.setText(text);
    }

    public void setTvGyro_z_text (String text) {
        tvGyro_z.setText(text);
    }
}
