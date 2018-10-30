package com.aoyanhao.coder.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aoyanhao.coder.utils.Utils;

/**
 * Created by 江俊超 on 2018/10/30.
 * Version:1.0
 * Description: 运动表盘View
 * ChangeLog:
 *
 * 使用 getTextBounds 来测量会有点问题，当文字进行变换的时候，
 * 测量坐标会不准确，应该换做FontMetrics来进行测量。
 *
 */
public class MotionDashView extends View {

    /**
     * 画笔
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 间距
     */
    private final float PADDING = Utils.dp2px(16);
    /**
     * 线条的宽度
     */
    private final float STROKE_WIDTH = Utils.dp2px(20);
    private final int FOR_GROUND = 0XFFff4181;
    private final int BACK_GROUND = 0XFF90a4ad;
    /**
     * 用来做背景的矩阵
     */
    private RectF mBoundRectF;
    /**
     * 测量文字的矩阵
     */
    private Rect mTextBoundRect;

    private String mText = "0";

    private int mSize = 0;
    private float maxCount = 8000;
    private int nowCount = 800;

    private Animator animator;

    private Paint.FontMetrics fontMetrics;

    public MotionDashView(Context context) {
        super(context);
    }

    public MotionDashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MotionDashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mTextBoundRect = new Rect();
        // 文字的X轴坐标为文字的中心，这样可以减少很多的计算量
        mPaint.setTextAlign(Paint.Align.CENTER);

        fontMetrics = new Paint.FontMetrics();
        mPaint.getFontMetrics(fontMetrics);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize = getWidth();
        // 1. 那个小，取哪个
        if (mSize > getHeight()) {
            mSize = getHeight();
        }
        // 2. 创建背景矩阵
        // left = width - size / 2
        // w = 100
        // s = 80
        // l = 10 + p
        mBoundRectF = new RectF((getWidth() - mSize) / 2 + PADDING + STROKE_WIDTH,
                (getHeight() - mSize) / 2 + PADDING + STROKE_WIDTH,
                mSize + (getWidth() - mSize) / 2 - PADDING - STROKE_WIDTH,
                mSize + (getHeight() - mSize) / 2 - PADDING - STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 3. 绘制底纹
        mPaint.setColor(BACK_GROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        canvas.drawArc(mBoundRectF, 0, 360, false, mPaint);

        // 4. 绘制外纹
        mPaint.setColor(FOR_GROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        float sweepAngle = 0;
        if (nowCount >= maxCount) {
            sweepAngle = 360;
        } else {
            sweepAngle = (nowCount / maxCount) * 360;
        }
        canvas.drawArc(mBoundRectF, -90, sweepAngle, false, mPaint);

        mText = String.valueOf(nowCount);


        // 5. 测量文字
//        mPaint.getTextBounds(mText, 0, mText.length(), mTextBoundRect);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(Utils.dp2px(85));
        // 6. 绘制文字
        canvas.drawText(mText,
                getWidth() / 2,
//                (mSize / 2 + PADDING * 2) - (mTextBoundRect.top + mTextBoundRect.bottom) / 2,
                (mSize / 2 + PADDING * 2) - (fontMetrics.descent + fontMetrics.ascent) / 2,
                mPaint);
    }

    public void startAmin(int targetCount) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        animator = ObjectAnimator.ofInt(this, "nowCount", 0, targetCount);
        animator.setDuration(1500);
//        animator.setInterpolator(new BounceInterpolator());
        animator.start();

    }


    public float getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public float getNowCount() {
        return nowCount;
    }

    public void setNowCount(int nowCount) {
        this.nowCount = nowCount;
        invalidate();
    }
}
