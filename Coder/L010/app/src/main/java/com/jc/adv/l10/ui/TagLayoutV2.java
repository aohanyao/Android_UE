package com.jc.adv.l10.ui;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江俊超 on 2018/12/4.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class TagLayoutV2 extends ViewGroup {

    private List<RectF> mBounds = new ArrayList<>();

    public TagLayoutV2(Context context) {
        super(context);
    }

    public TagLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 当前View的高度
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);

        // 已经使用了的高度
        int heightUsed = 0;
        // 已经使用的宽度
        int widthUsed = 0;
        // 每行的行高
        int lineHeight = 0;

        // 测量
        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            // 测量子View
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            // 增加margin
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            // 换行处理
            if ((childView.getMeasuredWidth() + widthUsed + layoutParams.leftMargin + layoutParams.rightMargin) > widthMeasure) {
                // 加上本行的高度
                heightUsed += lineHeight;
                lineHeight = 0;
                widthUsed = 0;
                // 重新进行测量
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }

            RectF childBound;
            if (mBounds.size() <= i) {
                childBound = new RectF();
                mBounds.add(childBound);
            } else {
                childBound = mBounds.get(i);
            }

            // 保存坐标
            childBound.left = widthUsed + layoutParams.leftMargin;
            childBound.right = widthUsed + childView.getMeasuredWidth() + layoutParams.rightMargin;
            childBound.top = heightUsed + layoutParams.topMargin;
            childBound.bottom = heightUsed + childView.getMeasuredHeight() + layoutParams.bottomMargin;

            // 保存最高的的高度
            lineHeight = Math.max(lineHeight, childView.getMeasuredHeight()+layoutParams.bottomMargin+layoutParams.topMargin);
            // 保存宽度
            widthUsed += childView.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;

        }

        setMeasuredDimension(widthMeasure, heightUsed + lineHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);

            RectF bound = mBounds.get(i);

            childAt.layout((int) bound.left, (int) bound.top, (int) bound.right, (int) bound.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
