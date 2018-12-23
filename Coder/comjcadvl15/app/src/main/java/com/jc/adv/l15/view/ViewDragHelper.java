package com.jc.adv.l15.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * view 拖拽
 */
public class ViewDragHelper extends ViewGroup {
    private final int ROW = 2;
    private final int COLUMN = 3;

    public ViewDragHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childWidth = specWidth / ROW;
        int childHeight = specHeight / COLUMN;

        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean isChange, int left, int top, int right, int bottom) {
        int childWidth = getWidth() / ROW;
        int childHeight = getHeight() / COLUMN;
        int childCount = getChildCount();
        int translationY = 0;
        // 遍历
        for (int index = 0; index < childCount; index++) {
            View childAt = getChildAt(index);
            // 全部都放在第一个点
            childAt.layout(0, 0, childWidth, childHeight);
            childAt.setTranslationY(translationY);

            // 然后每个进行偏移
            // 偏移是从 0开始的
            if (index % 2 == 1) {// 0 取余 2 为0 ，1取余2为1，
                // 换行的时候就加一次
                translationY += childHeight;
                childAt.setTranslationX(childWidth);
            } else {
                childAt.setTranslationX(0);
            }

        }
    }
}
