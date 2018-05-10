package com.t.araya.parka.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import com.t.araya.parka.R;


public class StartStopButtonViewGroup extends FrameLayout {
    private Button btnStart;
    private Button btnStop;

    public StartStopButtonViewGroup(@NonNull Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public StartStopButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public StartStopButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
    }

    public StartStopButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        // Inflate Layout here
        inflate(getContext(), R.layout.start_stop_button_layout, this);

    }

    private void initInstances() {
        // findViewById of item in ViewGroup here
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
    }

    public Button getBtnStart(){
        return btnStart;
    }

    public Button getBtnStop(){
        return btnStop;
    }
}
