package com.jc.adv.l10.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 正方形的ImageView
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = getMaxWidth();
        int measureHeight = getMeasuredHeight();
        int size = Math.min(measureHeight, measureWidth);
        setMeasuredDimension(size, size);
    }
}
