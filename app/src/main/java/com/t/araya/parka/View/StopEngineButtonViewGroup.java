package com.t.araya.parka.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import com.t.araya.parka.R;

public class StopEngineButtonViewGroup extends FrameLayout {

    private Button btnStopEngine;

    public StopEngineButtonViewGroup(@NonNull Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public StopEngineButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public StopEngineButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
    }

    public StopEngineButtonViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.stop_engine_button_layout, this);
    }

    private void initInstances() {
        btnStopEngine = findViewById(R.id.btnStopEngine);
    }

    public Button getBtnStopEngine(){
        return btnStopEngine;
    }

}
