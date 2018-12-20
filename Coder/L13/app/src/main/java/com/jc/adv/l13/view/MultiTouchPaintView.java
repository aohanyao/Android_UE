package com.jc.adv.l13.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 江俊超 on 2018/12/20.
 * Version:1.0
 * Description: 多点触摸的画图View
 * ChangeLog:
 */
public class MultiTouchPaintView extends View {
    private Paint mPaint;
    private Map<Integer, Path> mPath;
    private String TAG = "MultiTouchPaintView";

    public MultiTouchPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new LinkedHashMap<>();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPath.clear();
                Path mDownPath = new Path();
                // 设置初始位置
                mDownPath.moveTo(event.getX(), event.getY());
                mPath.put(event.getPointerId(event.getActionIndex()), mDownPath);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Path mPointerDownPath = new Path();
                // 设置初始位置
                mPointerDownPath.moveTo(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                mPath.put(event.getPointerId(event.getActionIndex()), mPointerDownPath);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG, "onTouchEvent: " + mPath.size());
                mPath.remove(event.getPointerId(event.getActionIndex()));
                Log.e(TAG, "onTouchEvent: " + mPath.size());
                break;

            case MotionEvent.ACTION_UP:
                mPath.clear();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                // 这里是进行了移动，需要判断是哪个点移动了吗？
                for (Map.Entry<Integer, Path> pathEntry : mPath.entrySet()) {
                    Integer pointerId = pathEntry.getKey();
                    pathEntry.getValue().lineTo(event.getX(event.findPointerIndex(pointerId)),
                            event.getY(event.findPointerIndex(pointerId)));
                }
                invalidate();
                break;

        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Map.Entry<Integer, Path> pathEntry : mPath.entrySet()) {
            Path value = pathEntry.getValue();
            canvas.drawPath(value, mPaint);

        }
    }
}
