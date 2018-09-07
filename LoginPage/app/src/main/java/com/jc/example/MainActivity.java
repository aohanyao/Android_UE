package com.jc.example;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jaeger.library.StatusBarUtil;
import com.jc.example.utils.DensityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        StatusBarUtil.setTranslucentForImageView(this, null);

        addLayoutListener(findViewById(R.id.llLogin));
    }

    /**
     * 1、大于屏幕整体高度的1/3：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于屏幕整体高度的1/3：键盘隐藏
     *
     * @param rootView 根布局
     */
    private void addLayoutListener(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();

                rootView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = rootView.getRootView().getHeight();
                int mainInvisibleHeight = rootView.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > screenHeight / 4) {
                    rootView.scrollTo(0, DensityUtils.dp2px(MainActivity.this, 200f/*需要滚动到图片目标高度*/));
                } else {
                    rootView.scrollTo(0, 0);
                }
            }
        });

    }
}
