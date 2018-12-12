package com.jc.adv.l13.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jc.adv.l13.R;
import com.jc.adv.l13.utils.Utils;

/**
 * Created by 江俊超 on 2018/12/12.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class TouchView1 extends View {
    private Bitmap mBitmap;
    private Paint mPaint;
    float mBitmapWidth = Utils.dp2px(200);

    private float mTranX = 0;
    private float mTranY = 0;
    private float downX;
    private float downY;

    public TouchView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 获取bitmap
        mBitmap = Utils.getDrawableBitmap(getContext(),
                R.drawable.icon_android_road, mBitmapWidth);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTranX = getWidth() / 2 - mBitmap.getWidth() / 2;
        mTranY = getHeight() / 2 - mBitmap.getHeight() / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mTranX = event.getX() - downX + mTranX;
                mTranY = event.getY() - downY + mTranY;
                invalidate();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;

        }


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mTranX, mTranY, mPaint);
    }
}
