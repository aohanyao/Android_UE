package com.aohanao.coder.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aohanao.coder.util.Utils;

public class DashboardView extends View {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRect;

    private final float START_ANAGLE = 135;
    // 半径
    private final float RADIUS = Utils.dp2px(150);
    // 间距
    private final float PADDING = Utils.dp2px(16);
    // 短刻度
    private Path mShortDash = new Path();
    // 长刻度
    private Path mLongDash = new Path();
    // 短的刻度
    private PathDashPathEffect mShortEffect;
    // 长刻度
    private PathDashPathEffect mLongEffect;

    // 最多的刻度
    private final int MAX_DASH = 50;

    public DashboardView(Context context) {
        super(context);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        // 样式为线条
        mPaint.setStyle(Paint.Style.STROKE);
        // 粗细
        mPaint.setStrokeWidth(Utils.dp2px(2));
        // 圆头
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 刻度
        mShortDash.addRect(0, 0, Utils.dp2px(2), Utils.dp2px(8), Path.Direction.CW);
        mLongDash.addRect(0, 0, Utils.dp2px(2), Utils.dp2px(15), Path.Direction.CW);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(getWidth() / 2 - RADIUS, PADDING, getWidth() / 2 + RADIUS, getHeight() - PADDING);

        // 刻度的数量
        Path arcPath = new Path();
        arcPath.addArc(mRect, START_ANAGLE, 360 - (START_ANAGLE - 45));

        PathMeasure pathMeasure = new PathMeasure(arcPath, false);

        float pathMeasureLength = pathMeasure.getLength();
        // 短刻度
        mShortEffect = new PathDashPathEffect(mShortDash,
                (pathMeasureLength - Utils.dp2px(2)) / 50,// 每个间距为多少
                0,// 第一个从什么地方开始
                PathDashPathEffect.Style.ROTATE);

        // 长刻度
        mLongEffect = new PathDashPathEffect(mLongDash,
                (pathMeasureLength - Utils.dp2px(2)) / 10,// 每个间距为多少
                0,// 第一个从什么地方开始
                PathDashPathEffect.Style.ROTATE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 135 °， 画了
        canvas.drawArc(mRect, START_ANAGLE, 360 - (START_ANAGLE - 45), false, mPaint);

        // 设置path
        mPaint.setPathEffect(mShortEffect);
        canvas.drawArc(mRect, START_ANAGLE, 360 - (START_ANAGLE - 45), false, mPaint);

        // 长刻度
        mPaint.setPathEffect(mLongEffect);
        canvas.drawArc(mRect, START_ANAGLE, 360 - (START_ANAGLE - 45), false, mPaint);


        // 画指针
//        canvas.drawLine(mRect.right / -RADIUS,
//                getHeight() / 2,
//                (float) Math.cos(Math.toDegrees(getCurrentAnagleForMark(5))),
//                (float) Math.sin(Math.toDegrees(getCurrentAnagleForMark(5))),
//                mPaint);
        // 三角函数的方式没画出来
        // 改用 旋转画布
        canvas.save();
        canvas.rotate(15,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth() / 2,
                (getHeight() - PADDING) / 2,
                getWidth() / 2 - RADIUS + PADDING+ Utils.dp2px(15),
                (getHeight() - PADDING) / 2,
                mPaint);

        canvas.restore();
    }

    private double getCurrentAnagleForMark(int mark) {
        return START_ANAGLE + (360 - (START_ANAGLE - 45)) / 20 * mark;
    }
}
