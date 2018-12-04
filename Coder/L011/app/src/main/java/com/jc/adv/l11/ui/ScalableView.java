package com.jc.adv.l11.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jc.adv.l11.R;
import com.jc.adv.l11.util.Utils;

/**
 * Created by 江俊超 on 2018/12/4.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class ScalableView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;

    private float mImageSize;

    // 最小缩放
    private float mMixScale;
    // 最大缩放
    private float mMaxScale;


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
        // 两种缩放方式：
        // 1. 左右贴满，上下留白(矮胖)
        // 2. 上下贴满，左右留白(高瘦)


    }

    private void init() {
        // 图片的大小
        mImageSize = Utils.dp2px(200);
        // 获取图片
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, mImageSize);
        // 画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 居中绘制 图片
        canvas.drawBitmap(mBitmap, (getWidth() - mImageSize) / 2, (getHeight() - mImageSize) / 2, mPaint);
    }
}
