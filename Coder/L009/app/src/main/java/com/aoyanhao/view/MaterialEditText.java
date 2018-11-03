package com.aoyanhao.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.aoyanhao.utils.Utils;

public class MaterialEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher {

    private final float PADDING = Utils.dp2px(18);
    private final float HIT_X_OFFSET = Utils.dp2px(12);
    private final float HIT_Y_OFFSET = Utils.dp2px(4);


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
        mPaint.setTextSize((float) (getTextSize() * 0.8));
        mPaint.setColor(0xffbbbbbb);
        addTextChangedListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制 hint
        if (getHint() != null) {
            String hint = getHint().toString();
            canvas.drawText(hint, getPaddingLeft(), HIT_X_OFFSET, mPaint);
        }
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

    }
}
