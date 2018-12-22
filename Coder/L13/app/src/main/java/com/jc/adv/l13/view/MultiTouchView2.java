package com.jc.adv.l13.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jc.adv.l13.R;
import com.jc.adv.l13.utils.Utils;

/**
 * 多点合作的view
 */
public class MultiTouchView2 extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mTranX;
    private int mOriginX;
    private float mDownX;
    private int mTranY;
    private int mOriginY;
    private float mDownY;

    public MultiTouchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = Utils.getDrawableBitmap(getContext(),
                R.drawable.icon_android_road, Utils.dp2px(200));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTranX = getWidth() / 2 - mBitmap.getWidth() / 2;
        mTranY = getHeight() / 2 - mBitmap.getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(mBitmap, mTranX, mTranY, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int sumX = 0;
        int sumY = 0;
        // 忽略掉 抬起的那个手指
        boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;

        for (int i = 0; i < pointerCount; i++) {
            if (!(isPointerUp && event.getActionIndex() == i)) {
                sumX += event.getX(i);
                sumY += event.getY(i);
            }
        }

        if (isPointerUp) pointerCount--;
        float focusX = sumX / pointerCount;
        float focusY = sumY / pointerCount;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 单指按下，多指按下和多个手指抬起
                mDownX = focusX;
                mDownY = focusY;
                mOriginY = mTranY;
                mOriginX = mTranX;
                break;

            case MotionEvent.ACTION_MOVE:
                mTranY = (int) (mOriginY + focusY - mDownY);
                mTranX = (int) (mOriginX + focusX - mDownX);
                invalidate();
                break;
        }

        return true;
    }
}
