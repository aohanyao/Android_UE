package com.aoyanhao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aoyanhao.coder.view.CameraRotaeView;
import com.aoyanhao.coder.view.MotionDashView;

public class MainActivity extends AppCompatActivity {

    private MotionDashView mMotionDashView;
    private CameraRotaeView mCameraRotaeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startMotionDash(View view) {
        if (mMotionDashView == null) {
            mMotionDashView = findViewById(R.id.mMotionDashView);
        }

        // 开始动画
        mMotionDashView.startAmin(5000);
    }

    public void startCameraRotaeView(View view) {
        if (mCameraRotaeView == null) {
            mCameraRotaeView = findViewById(R.id.mCameraRotaeView);
        }
        mCameraRotaeView.startAmin();
    }
}
