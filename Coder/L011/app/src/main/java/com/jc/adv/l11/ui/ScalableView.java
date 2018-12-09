package com.jc.adv.l11.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.jc.adv.l11.R;
import com.jc.adv.l11.util.Utils;

/**
 * Created by 江俊超 on 2018/12/4.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class ScalableView extends View implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener, Runnable {
    private final String TAG = getClass().getSimpleName();

    private Bitmap mBitmap;
    private Paint mPaint;

    private float mImageSize;

    // 最小缩放
    private float mMixScale;
    // 最大缩放
    private float mMaxScale;

    private float mOriginOffsetX = 0;
    private float mOffsetX = 0;
    private float mOriginOffsetY = 0;
    private float mOffsetY = 0;

    private float mBitmatOriginalOffsetX;
    private float mBitmatOriginalOffsetY;

    private float mDownX;
    private float mDownY;

    private boolean isBig = false;

    private GestureDetectorCompat gestureDetectorCompat;

    private float scalePre = 0;
    private ObjectAnimator scaleAmin;

    private float tempScale = 1.5f;

    private OverScroller scroller;


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
//        mOffsetX = (getWidth() - mImageSize) / 2;
        mBitmatOriginalOffsetX = (getWidth() - mImageSize) / 2;
//        mOffsetX = (getHeight() - mImageSize) / 2;
        mBitmatOriginalOffsetY = (getHeight() - mImageSize) / 2;

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
        scroller = new OverScroller(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 进行触摸偏移
        canvas.translate(mOriginOffsetX * scalePre, mOriginOffsetY * scalePre);


        float scale = mMixScale + ((mMaxScale - mMixScale) * scalePre);

        //缩放
        canvas.scale(scale, scale, getWidth() / 2, getHeight() / 2);
        // 居中绘制 图片
        canvas.drawBitmap(mBitmap, mBitmatOriginalOffsetX, mBitmatOriginalOffsetY, mPaint);
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
        // 进行判断，进行双击中的点 进行中心进行缩放
        isBig = !isBig;
        Log.e(TAG, "onDoubleTapEvent: " + e.getX());


        // 从小到大
        if (isBig) {
            // 中心点  TODO 这个公式还不是很清楚，需要进行更深一步的理解
            mOriginOffsetX = (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2) * mMaxScale / mMixScale;
            mOriginOffsetY = (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2) * mMaxScale / mMixScale;
//            Log.e(TAG, "onDoubleTap: " + mOffsetX);
            getScaleAmin().start();
        } else {
            // 从大到小
            // 进行回滚，然后进行X轴的变化
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
        // distanceX 负数为 向右边拖动，为正数为向左边拖动
        // 当前点为(0,0),向左边不得小于 -offsetLimit 的距离，向右不得大于 offsetLimit
        if (isBig) {// 在放大的状态下才进行拖动
            //进行了滚动
            // 以(0,0)为原点
            mOriginOffsetX -= distanceX;
            mOriginOffsetY -= distanceY;
            // 边界判断
            // 最右不能小于图片的宽度减去View的宽度除以二，最左不能大于图片的宽度减去View的宽度除以二
            // 最上和最下是一个道理
            handlerPort();


            postInvalidate();
        }

        return false;
    }

    /**
     * 处理边界情况
     */
    private void handlerPort() {
        float offsetLimitX = (mBitmap.getWidth() * mMaxScale - getWidth()) / 2;
        if (mOriginOffsetX <= -offsetLimitX) {
            mOriginOffsetX = Math.max(mOriginOffsetX, -offsetLimitX);
        }
        if (mOriginOffsetX >= offsetLimitX) {
            mOriginOffsetX = Math.min(mOriginOffsetX, offsetLimitX);
        }

        // 上下边界的判定
        float offsetLimitY = (mBitmap.getHeight() * mMaxScale - getHeight()) / 2;
        if (mOriginOffsetY <= -offsetLimitY) {
            mOriginOffsetY = Math.max(mOriginOffsetY, -offsetLimitY);
        }
        if (mOriginOffsetY >= offsetLimitY) {
            mOriginOffsetY = Math.min(mOriginOffsetY, offsetLimitY);
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (isBig) {
            Log.e(TAG, "onFling: ");
            float offsetLimitX = (mBitmap.getWidth() * mMaxScale - getWidth()) / 2;
            float offsetLimitY = (mBitmap.getHeight() * mMaxScale - getHeight()) / 2;

            scroller.fling((int) mOffsetX,
                    (int) mOffsetY,
                    (int) velocityX,
                    (int) velocityY,
                    (int) -offsetLimitX,
                    (int) offsetLimitX,
                    (int) -offsetLimitY,
                    (int) offsetLimitY);
            ViewCompat.postInvalidateOnAnimation(this);
        }

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
        handlerPort();
        postInvalidate();
    }

    @Override
    public void run() {
        if (scroller.computeScrollOffset()) {
            mOffsetX = scroller.getCurrX();
            mOffsetY = scroller.getCurrY();
            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
