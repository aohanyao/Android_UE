package com.aoyanhao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aoyanhao.coder.view.MotionDashView;

public class MainActivity extends AppCompatActivity {

    private MotionDashView mMotionDashView;

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
}
