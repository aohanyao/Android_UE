package com.jc.adv.l11.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jc.adv.l11.R;
import com.jc.adv.l11.util.Utils;

/**
 * Created by 江俊超 on 2018/12/4.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class ScalableView extends View implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {

    private Bitmap mBitmap;
    private Paint mPaint;

    private float mImageSize;

    // 最小缩放
    private float mMixScale;
    // 最大缩放
    private float mMaxScale;

    private float mOffsetX;
    private float mOffsetY;
    private float mOriginalOffsetX;
    private float mOriginalOffsetY;

    private boolean isBig = false;

    private GestureDetectorCompat gestureDetectorCompat;

    private float scalePre = 0;
    private ObjectAnimator scaleAmin;

    private float tempScale = 1.5f;


    public ScalableView(Context context) {
        super(context);
        init();
    }


    public ScalableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScalableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 定义两个中间值
        mOriginalOffsetX = (getWidth() - mImageSize) / 2;
        mOriginalOffsetY = (getHeight() - mImageSize) / 2;

        // 两种缩放方式：
        // 1. 左右贴满，上下留白(矮胖)
        // 2. 上下贴满，左右留白(高瘦)

        // 图片的宽除以高大于页面的宽除以高 矮胖
        if (((float) mBitmap.getWidth() / mBitmap.getHeight()) > (getWidth() / getHeight())) {
            // 矮胖的情况下，最小就是 View除以bitmap的宽度
            mMixScale = (float) getWidth() / mBitmap.getWidth();
            mMaxScale = (float) getHeight() / mBitmap.getHeight() * tempScale;
        } else {
            mMaxScale = (float) getWidth() / mBitmap.getWidth();
            mMixScale = (float) getHeight() / mBitmap.getHeight() * tempScale;
        }


    }

    private void init() {
        // 图片的大小
        mImageSize = Utils.dp2px(300);
        // 获取图片
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, mImageSize);
        // 画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), this);
        gestureDetectorCompat.setOnDoubleTapListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 进行触摸偏移
        canvas.translate(mOffsetX, mOffsetY);


        float scale = mMixScale + ((mMaxScale - mMixScale) * scalePre);

        //缩放
        canvas.scale(scale, scale, getWidth() / 2, getHeight() / 2);
        // 居中绘制 图片
        canvas.drawBitmap(mBitmap, mOriginalOffsetX, mOriginalOffsetY, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        isBig = !isBig;
        if (isBig) {
            // 从小到大
            getScaleAmin().start();
        } else {
            // 从大到小
//            mOffsetY=0;
//            mOffsetX=0;
            getScaleAmin().reverse();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 返回true，代表事件被消费
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isBig) {// 在放大的状态下才进行拖动
            //进行了滚动
            // 以(0,0)为原点
            mOffsetX -= distanceX;
            mOffsetY -= distanceY;
            // 边界判断
            // 最右不能小于图片的宽度减去View的宽度除以二，最左不能大于图片的宽度减去View的宽度除以二
            // 最上和最下是一个道理
            // 2018年12月04日22:33:56 明天进行复刻
            postInvalidate();
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private ObjectAnimator getScaleAmin() {
        if (scaleAmin == null) {
            scaleAmin = ObjectAnimator.ofFloat(this, "scalePre", 0, 1);
        }
        return scaleAmin;
    }

    public float getScalePre() {
        return scalePre;
    }

    public void setScalePre(float scalePre) {
        this.scalePre = scalePre;
        postInvalidate();
    }
}
