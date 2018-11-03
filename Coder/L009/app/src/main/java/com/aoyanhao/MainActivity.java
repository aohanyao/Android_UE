package com.aoyanhao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aoyanhao.view.DashView;

public class MainActivity extends AppCompatActivity {

    private DashView mDashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDashView(View view) {
        if (mDashView == null) {
            mDashView = findViewById(R.id.mDashView);
        }

        mDashView.start();
    }
}
