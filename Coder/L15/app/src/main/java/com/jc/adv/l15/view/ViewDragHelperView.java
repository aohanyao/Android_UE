package com.jc.adv.l15.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/***
 * ViewDragHelper 实现拖动
 */
public class ViewDragHelperView extends ViewGroup {

    private final int ROW = 2;
    private final int COLUMN = 3;

    private ViewDragHelper mViewDragHelper;
    private ViewDragHelperCallback viewDragHelperCallback;

    public ViewDragHelperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        viewDragHelperCallback = new ViewDragHelperCallback();
        mViewDragHelper = ViewDragHelper.create(this, viewDragHelperCallback);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthSpec = specWidth / ROW;
        int childHeightSpec = specHeight / COLUMN;

        // 测量子view
        measureChildren(MeasureSpec.makeMeasureSpec(childWidthSpec, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeightSpec, MeasureSpec.EXACTLY));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidth = getWidth() / ROW;
        int childHeight = getHeight() / COLUMN;

        int childLeft = 0;
        int childTop = 0;
        int childRight = childWidth;
        int childBottom = childHeight;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            // 0 1
            // 2 3
            // 4 5
            if (i % 2 == 0) {// 第一行
                childLeft = 0;
                childRight = childWidth;
            } else {// 第二行
                childLeft = childWidth;
                childRight += childWidth;
            }
            childAt.layout(childLeft, childTop, childRight, childBottom);
            // 换行
            if (i % 2 == 1) {
                childTop += childHeight;
                childBottom += childHeight;
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    private class ViewDragHelperCallback extends ViewDragHelper.Callback {
        private int capturedChildLeft;
        private int capturedChildTop;

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (state == ViewDragHelper.STATE_IDLE) {
                View captureView = mViewDragHelper.getCapturedView();
                if (captureView != null) {
                    captureView.setElevation(captureView.getElevation() - 1);
                }
            }
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            capturedChildLeft = capturedChild.getLeft();
            capturedChildTop = capturedChild.getTop();
            capturedChild.setElevation(capturedChild.getElevation() + 1);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            mViewDragHelper.settleCapturedViewAt(capturedChildLeft, capturedChildTop);
            postInvalidateOnAnimation();

        }
    }
}
