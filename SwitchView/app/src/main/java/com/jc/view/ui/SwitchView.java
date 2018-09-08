package com.jc.view.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jc.view.R;
import com.jc.view.utils.DensityUtils;
import com.jc.view.utils.MeasureSpecUtisl;

/**
 * Created by jc on 2016/12/22 0022.
 * <p>Gihub https://github.com/aohanyao</p>
 * <p>SwitchView</p>
 * <p>2017年7月20日11:53:48，修改了文字居中的问题，修改了测量的方式，使用矩形来进行测量
 * <br/>
 * 但是测量的精准度不够，使用了高度的0.7左右。 这个数字没有什么依据，只是一次一次的试出来的
 * </p>
 */
public class SwitchView extends View implements View.OnClickListener {
    /**
     * 关闭文字
     */
    private String offText;
    /**
     * 打开文字
     */
    private String onText;
    private int onTextColor = 0xFFFFFFFF;
    private int offTextColor = 0xFF000000;
    private int mTempTextColor = 0xFF000000;
    private int mBackgroundColor = 0xFFFFFFFF;
    private int mOnBackgroundColor = 0xFF03A9F4;
    private int mOffBackgroundColor = 0xFFFF0000;
    /**
     * 文字大小
     */
    private float textSize;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private RectF mBackgroundRectf;
    private RectF mOnRectf;
    private int mCenterHeight;
    /**
     * 前面那个按钮的宽度
     */
    private float mFrontGroundWidth;
    private String TAG = SwitchView.class.getSimpleName();
    private float onTextCenterHeight;
    private int mCenterWidth;
    private float offTextCenterHeight;
    //是否已经被打开
    private boolean isOn = true;
    private float aminValueHundred = 0;
    private ValueAnimator valueAnimator;
    //是否已经交换颜色
    private boolean isExchangeColor = false;
    private onSwitchListener onSwitchListener;
    private Rect mRect;

    /**
     * 圆角
     */
    private float mRadius = 0;

    public void setOnSwitchListener(SwitchView.onSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context 上下午
     * @param attrs   属性
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        //关闭文字
        offText = typedArray.getString(R.styleable.SwitchView_off_text);
        offText = TextUtils.isEmpty(offText) ? "关闭" : offText;
        //打开文字
        onText = typedArray.getString(R.styleable.SwitchView_on_text);
        onText = TextUtils.isEmpty(onText) ? "打开" : onText;
        //关闭文字颜色
        offTextColor = typedArray.getColor(R.styleable.SwitchView_off_text_color, offTextColor);
        //打开文字颜色
        onTextColor = typedArray.getColor(R.styleable.SwitchView_on_text_color, onTextColor);
        //背景颜色
        mBackgroundColor = typedArray.getColor(R.styleable.SwitchView_background_color, mBackgroundColor);
        //打开背景颜色
        mOnBackgroundColor = typedArray.getColor(R.styleable.SwitchView_on_background_color, mOnBackgroundColor);
        //关闭背景颜色
        mOffBackgroundColor = typedArray.getColor(R.styleable.SwitchView_off_background_color, mOnBackgroundColor);
        //文字大小
        textSize = typedArray.getDimension(R.styleable.SwitchView_text_size, 16);
        //圆角
        mRadius = typedArray.getDimension(R.styleable.SwitchView_radius, mRadius);
        //前面那个按钮的长度
        mFrontGroundWidth = typedArray.getDimension(R.styleable.SwitchView_front_ground_width, 0);

        typedArray.recycle();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        //点击事件
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量宽度与高度
        int measureWidth = MeasureSpecUtisl.measureSpecSize(widthMeasureSpec, DensityUtils.dp2px(getContext(), 100));
        int measureHeight = MeasureSpecUtisl.measureSpecSize(heightMeasureSpec, DensityUtils.dp2px(getContext(), 30));
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //View的宽度
        mWidth = w;
        //View的高度
        mHeight = h;
        //高度的中间
        mCenterHeight = h / 2;
        //宽度的中间
        mCenterWidth = w / 2;
        //创建背景矩形
        mBackgroundRectf = new RectF(0, 0, mWidth, mHeight);
        //打开的矩形
        mOnRectf = new RectF(0, 0, mCenterWidth, mHeight);
        //文字的中间高度
        Rect mRect = new Rect();
        mPaint.setTextSize(textSize);
        // 测量打开文字
        mPaint.getTextBounds(onText, 0, onText.length(), mRect);
        onTextCenterHeight = mRect.height() * 0.4f;
        //测量关闭文字
        mPaint.getTextBounds(offText, 0, offText.length(), mRect);
        offTextCenterHeight = mRect.height() * 0.4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 更改颜色
        mPaint.setColor(mBackgroundColor);
        // 绘制背景矩形
        canvas.drawRoundRect(mBackgroundRectf, mRadius, mRadius, mPaint);


        //当前百分比的宽度
        int valueWidth = (int) (mCenterWidth * aminValueHundred);
        if (isOn) {
            //打开
            mOnRectf = new RectF(0 + valueWidth, 0, mCenterWidth + valueWidth, mHeight);
            mPaint.setColor(mOnBackgroundColor);
            if (aminValueHundred >= 0.5 && !isExchangeColor) {
                ////置换两种颜色
                mTempTextColor = offTextColor;
                offTextColor = onTextColor;
                onTextColor = mTempTextColor;
                isExchangeColor = true;
            }
            if (aminValueHundred >= 0.5) {
                mPaint.setColor(mOffBackgroundColor);
            }
        } else {
            //关闭
            mOnRectf = new RectF(mCenterWidth - valueWidth, 0, mWidth - valueWidth, mHeight);
            mPaint.setColor(mOffBackgroundColor);
            if (aminValueHundred >= 0.5 && !isExchangeColor) {
                //置换两种颜色
                mTempTextColor = onTextColor;
                onTextColor = offTextColor;
                offTextColor = mTempTextColor;
                isExchangeColor = true;
            }
            if (aminValueHundred >= 0.5) {
                mPaint.setColor(mOnBackgroundColor);
            }
        }
        if (!isOn && aminValueHundred == 1 && valueAnimator == null) {
            mOnRectf = new RectF(valueWidth, 0, mWidth, mHeight);
            mPaint.setColor(mOffBackgroundColor);
        }

        canvas.drawRoundRect(mOnRectf, mRadius, mRadius, mPaint);

        //绘制打开文字
        mPaint.setColor(onTextColor);
        mPaint.setTextSize(textSize);
        canvas.drawText(onText, mCenterWidth / 2 - mPaint.measureText(onText) / 2, mCenterHeight + onTextCenterHeight, mPaint);


        //绘制关闭文字
        mPaint.setColor(offTextColor);
        mPaint.setTextSize(textSize);
        canvas.drawText(offText, (mCenterWidth + mCenterWidth / 2) - mPaint.measureText(offText) / 2, mCenterHeight + offTextCenterHeight, mPaint);
        // 动画结束
        if (aminValueHundred == 1 && valueAnimator != null) {
            valueAnimator = null;
            isOn = !isOn;
            if (onSwitchListener != null) {
                onSwitchListener.onSwitchListener(isOn, isOn ? onText : offText);
            }

        }
    }

    /**
     * 开关
     *
     * @param off
     */
    public void setSwitch(boolean off) {
        if (isOn != off) {
            startAnim();
        }
    }

    @Override
    public void onClick(View v) {
        startAnim();
    }

    private void startAnim() {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            //发散一个宽度的值
            valueAnimator = ValueAnimator.ofFloat(1).setDuration(300);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    aminValueHundred = (float) animation.getAnimatedValue();
                    invalidate();
                }

            });
            isExchangeColor = false;
            valueAnimator.start();
        }
    }

    public String getOffText() {
        return offText;
    }


    public void setOffText(@NonNull String offText) {
        this.offText = offText;
    }

    public String getOnText() {
        return onText;
    }

    public void setOnText(@NonNull String onText) {
        this.onText = onText;
    }


    public interface onSwitchListener {
        /**
         * @param isOn       是否已经打开
         * @param swithcText 现在选中的文字。
         */
        void onSwitchListener(boolean isOn, String swithcText);
    }
}