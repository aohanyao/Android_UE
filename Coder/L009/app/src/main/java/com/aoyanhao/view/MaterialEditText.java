package com.aoyanhao.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.aoyanhao.utils.Utils;

public class MaterialEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher {

    private final float PADDING = Utils.dp2px(18);
    private final float HIT_Y = Utils.dp2px(18);

    private final float TEXT_Y_OFFSET = Utils.dp2px(17);
    /**
     * Y轴的偏移量
     */
    private float textYOffset = -1;
    /**
     * 透明底
     */
    private float textAlpha = 1f;
    /**
     * 是否已经动画到顶部了
     */
    private boolean isHitTop = false;


    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public MaterialEditText(Context context) {
        super(context);
    }

    public MaterialEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setPadding(getPaddingLeft(), (int) (getPaddingTop() + PADDING), getPaddingRight(), getPaddingBottom());
        mPaint.setTextSize((float) (getTextSize() * 0.9));
        mPaint.setColor(0xffD81B60);
        addTextChangedListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制 hint
        if (getHint() != null) {
            String hint = getHint().toString();
            if (textYOffset < TEXT_Y_OFFSET && textYOffset != -1) {
                mPaint.setAlpha((int) (textAlpha * 255));
                canvas.drawText(hint, getPaddingLeft(), HIT_Y + textYOffset, mPaint);

            }
        }
    }

    public float getTextYOffset() {
        return textYOffset;
    }

    public void setTextYOffset(float textYOffset) {
        this.textYOffset = textYOffset;
        postInvalidate();
    }

    public float getTextAlpha() {
        return textAlpha;
    }

    public void setTextAlpha(float textAlpha) {
        this.textAlpha = textAlpha;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        startHintAnimation();
    }

    private void startHintAnimation() {
        // 移动偏移量
        boolean isEmptyText = getText() == null || TextUtils.isEmpty(getText().toString());
        ObjectAnimator textYOffsetAnimator = null;
        ObjectAnimator textAlphaAnimator = null;
        if (isEmptyText) {
            // 当前是空字符串
            textYOffsetAnimator = ObjectAnimator.ofFloat(this, "textYOffset", 0, TEXT_Y_OFFSET);
            textAlphaAnimator = ObjectAnimator.ofFloat(this, "textAlpha", 1f, 0.15f);
            isHitTop = false;
        } else {
            // 没有执行过动画
            if (getText().toString().length() == 1 && !isHitTop) {
                textYOffsetAnimator = ObjectAnimator.ofFloat(this, "textYOffset", TEXT_Y_OFFSET, 0);
                textAlphaAnimator = ObjectAnimator.ofFloat(this, "textAlpha", 0.15f, 1f);
                isHitTop = true;
            }
        }
        if (textYOffsetAnimator != null && textAlphaAnimator != null) {
            // 同时动画
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(textYOffsetAnimator, textAlphaAnimator);
            animatorSet.setDuration(300);
            animatorSet.start();

        }


    }
}
