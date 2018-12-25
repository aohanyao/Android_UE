package com.jc.adv.l15.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 江俊超 on 2018/12/25.
 * Version:1.0
 * Description: 自己重写，第二版本的dragView
 * ChangeLog:
 */
public class ViewDragHelperView2 extends ViewGroup {
    private final int ROW = 2;
    private final int COLUMN = 3;

    private ViewDragHelper mViewDragHelper;
    private ViewDragHelperCallBack mViewDragHelperCallBack;

    public ViewDragHelperView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewDragHelperCallBack = new ViewDragHelperCallBack();
        mViewDragHelper = ViewDragHelper.create(this, mViewDragHelperCallBack);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1. 测量和布局子view

        // 1.1 获取到我的预测宽高
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 1.2 计算每个子view的宽度
        int childWidth = specWidth / ROW;
        int childHeight = specHeight / COLUMN;

        // 1.3 分别测量子view
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 2.摆放每个子view的位置

        // 2.1 子view的宽高
        int childWidth = getWidth() / ROW;
        int childHeight = getHeight() / COLUMN;

        // 2.2 子view的左上右下
        int childLeft = 0;
        int childTop = 0;
        int childRight = childWidth;
        int childBottom = childHeight;

        int childCount = getChildCount();
        // 遍历每个子view
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            // 计算位置
            if (i % 2 == 1) {
                // 第一列
                childLeft = 0;
                childRight = childWidth;
            } else {
                childLeft += childWidth;
                childRight += childRight;
            }
            childAt.layout(childLeft, childTop, childRight, childBottom);
            if (i % 2 == 1) {
                childTop += childHeight;
                childBottom += childHeight;
            }
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        private int mClampViewLeft;
        private int mClampViewTop;

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return true;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            mClampViewLeft = capturedChild.getLeft();
            mClampViewTop = capturedChild.getTop();
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            mViewDragHelper.settleCapturedViewAt(mClampViewLeft, mClampViewTop);
            postInvalidateOnAnimation();
        }


        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }
    }
}
