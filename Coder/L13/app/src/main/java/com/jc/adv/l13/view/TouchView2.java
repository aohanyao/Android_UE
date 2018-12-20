package com.jc.adv.l13.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jc.adv.l13.R;
import com.jc.adv.l13.utils.Utils;

/**
 * Created by 江俊超 on 2018/12/20.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class TouchView2 extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private float mBitmapWidth = Utils.dp2px(200);

    private int mTranX;
    private int mTranY;
    private int mWidth;
    private int mHeight;

    private float mDownX;
    private float mDownY;

    private int mTrankPointerId;

    public TouchView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, mBitmapWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTranX = getWidth() / 2 - mBitmap.getWidth() / 2;
        mTranY = getHeight() / 2 - mBitmap.getHeight() / 2;
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mTrankPointerId = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerDownIndex = event.getActionIndex();
                mTrankPointerId = event.getPointerId(pointerDownIndex);
                mDownY = event.getY(pointerDownIndex);
                mDownX = event.getX(pointerDownIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int movePointerIndex = event.findPointerIndex(mTrankPointerId);
                if (movePointerIndex != -1) {
                    mTranX = (int) (mTranX - (mDownX - event.getX(movePointerIndex)));
                    mTranY = (int) (mTranY - (mDownY - event.getY(movePointerIndex)));
                    mDownX = event.getX(movePointerIndex);
                    mDownY = event.getY(movePointerIndex);
                    handlerPort();
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 抬起的是当前活跃的手指
                if (mTrankPointerId == event.getPointerId(event.getActionIndex())) {
                    int newPointerIndex;
                    // 必须要
                    if (event.getActionIndex() == event.getPointerCount() - 1 && event.getPointerCount() > 2) {
                        newPointerIndex = event.getPointerCount() - 2;
                    } else {
                        newPointerIndex = event.getPointerCount() - 1;
                    }
                    mDownX = event.getX(newPointerIndex);
                    mDownY = event.getY(newPointerIndex);
                    handlerPort();
                    invalidate();
                }
                break;
        }


        return true;
    }

    /**
     * 处理边界碰撞
     */
    private void handlerPort() {
        mTranX = Math.max(0, mTranX);
        mTranX = Math.min(mTranX, mWidth - mBitmap.getWidth());

        mTranY = Math.max(0, mTranY);
        mTranY = Math.min(mTranY, mHeight - mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(mBitmap, mTranX, mTranY, mPaint);
    }
}
