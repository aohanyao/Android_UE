package com.jc.adv.l10.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by 江俊超 on 2018/12/3.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class TagChildView extends android.support.v7.widget.AppCompatTextView {
    private RectF mBackgroundRectF;

    private Paint mPaint;


    public TagChildView(Context context) {
        super(context);
    }

    public TagChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length()==1 ? "0" + red : red ;
        //判断绿色代码的位数
        green = green.length()==1 ? "0" + green : green ;
        //判断蓝色代码的位数
        blue = blue.length()==1 ? "0" + blue : blue ;
        //生成十六进制颜色值
        String color = "#"+red+green+blue;


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor(color));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBackgroundRectF = new RectF(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mBackgroundRectF, 20, 20, mPaint);
        super.draw(canvas);
    }
}
