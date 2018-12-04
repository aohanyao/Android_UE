package com.jc.adv.l10.ui;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江俊超 on 2018/12/3.
 * Version:1.0
 * Description:
 * ChangeLog:
 * ----------------------------------------------------
 * 自定义View中，测量，布局是一种思路。
 * ----------------------------------------------------
 */

public class TagLayout extends ViewGroup {
    // 用来存每个View的位置
    private List<RectF> mTagBounds = new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1. 测量高度，测量宽度
        // 2. 同一行中高度最高的是高度，宽度不能大于View的宽度
        // 总宽度
        float sumWidth = 0;
        // 行高度
        float lineHeight = 0;
        // 已经使用的宽度
        int widhtUse = 0;
        // 整个View的宽度
        int widhtSpec = MeasureSpec.getSize(widthMeasureSpec);

        //已经使用了的高度
        int heightUse = 0;


        // 遍历所有的子View
        for (int i = 0; i < getChildCount(); i++) {
            // 获取子View
            View childView = getChildAt(i);

            // 测量子view
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUse);

            // 获取到margin
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();


            // 进行换行处理   加上了左右的的外边距
            if ((widhtUse + childView.getMeasuredWidth() + layoutParams.leftMargin
                    + layoutParams.rightMargin > widhtSpec)) {
                // 增加高度
                heightUse = (int) (heightUse + lineHeight);
                // 宽度
                sumWidth = Math.max(sumWidth, widhtUse);
                widhtUse = 0;
                // 重新处理
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUse);

            }

            // 取出存储坐标的矩形
            RectF bound;
            if (mTagBounds.size() <= i) {
                bound = new RectF();
                mTagBounds.add(bound);
            } else {
                bound = mTagBounds.get(i);
            }

            // 设置位置
            bound.left = widhtUse + layoutParams.leftMargin;
            bound.top = heightUse + layoutParams.topMargin;
            bound.right = widhtUse + childView.getMeasuredWidth() + layoutParams.rightMargin;
            bound.bottom = heightUse + childView.getMeasuredHeight() + layoutParams.bottomMargin;

            // 对比行高
            lineHeight = Math.max(childView.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin,
                    lineHeight);
            // 宽度的使用要加上本次 子view的宽度
            widhtUse = widhtUse + childView.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
        }
        heightUse += lineHeight;

        setMeasuredDimension((int) sumWidth, heightUse);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            RectF bound = mTagBounds.get(i);
            View childView = getChildAt(i);
            childView.layout((int) bound.left, (int) bound.top, (int) bound.right, (int) bound.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}
