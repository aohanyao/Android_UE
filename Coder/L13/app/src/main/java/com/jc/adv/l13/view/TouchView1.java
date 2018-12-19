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
    private final String TAG = getClass().getSimpleName();
    private float mTranX = 0;
    private float mTranY = 0;
    private float downX;
    private float downY;

    private int trankId;
    private int mHeight;
    private int mWidth;

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
        mWidth = getWidth();
        mHeight = getHeight();
    }

    // 一个手指，进行拖动
    // 两个手指，第二个手指进行拖动，第二个手指抬起，第一个手指拖动
    // 三个手指，第三个手机进行拖动，第三个手指抬起，第二个手指拖动，第二个手指抬起，第一个手指拖动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                trankId = event.getPointerId(event.getActionIndex());
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                trankId = event.getPointerId(event.getActionIndex());
                int downIndex = event.getActionIndex();
                downX = event.getX(downIndex);
                downY = event.getY(downIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveIndex = event.findPointerIndex(trankId);
                if (moveIndex >= 0) {
                    mTranX = event.getX(moveIndex) - downX + mTranX;
                    mTranY = event.getY(moveIndex) - downY + mTranY;
                    handlerPort();
                    invalidate();
                    downX = event.getX(moveIndex);
                    downY = event.getY(moveIndex);

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 抬起的是当前的手指
                if (event.getPointerId(event.getActionIndex()) == trankId && event.getPointerCount() > 1) {
                    int newIndex;

                    if (event.getActionIndex() == event.getPointerCount() - 1 && event.getPointerCount() > 2) {
                        newIndex = event.getActionIndex() - 2;
                    } else {
                        newIndex = event.getActionIndex() - 1;
                    }

                    if (newIndex >= 0) {
                        trankId = event.getPointerId(newIndex);
                        downX = event.getX(newIndex);
                        downY = event.getY(newIndex);
                    }
                }

                break;

        }


        return true;
    }

    /**
     * 处理边界情况
     */
    void handlerPort() {
        mTranX = Math.max(0, mTranX);
        mTranX = Math.min(mWidth - mBitmap.getWidth(), mTranX);


        mTranY = Math.max(0, mTranY);
        mTranY = Math.min(mHeight - mBitmap.getHeight(), mTranY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mTranX, mTranY, mPaint);
    }
}
