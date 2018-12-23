package com.jc.adv.l14.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * 有两页的Pager
 * ----------------------------------------------
 * 技术实现点：
 * 1. 测量布局
 * 2. 摆放子view的位置
 * 3. 判断事件拦截（VelocityTracker，速度追踪器）
 * 4. 监听touch_move事件（onInterceptTouchEvent）
 * 5. 使用速度追踪器追踪当前手指滑动的速度
 * 5.1 ViewConfiguration获取相关的配置
 * 5.2 做滑动距离的判断，速度追踪器是用来计算我滑动的距离的，
 * 这样就不用自己来做滑动距离的判断。
 * 6. 使用OverScroller进行动态滑动
 * computeScroll这个方法在每一帧绘制的时候都会进行调用，
 * ----------------------------------------------
 */
public class TowPageViewPager extends ViewGroup {


    private VelocityTracker velocityTracker;

    private String TAG = getClass().getSimpleName();
    private float mDownX;

    private boolean isScrolling = false;
    private float mDownScrolerX;
    private ViewConfiguration viewConfiguration;
    private OverScroller overScroller;
    private int maximumFlingVelocity;
    private int minimumFlingVelocity;

    public TowPageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        velocityTracker = VelocityTracker.obtain();
        viewConfiguration = ViewConfiguration.get(getContext());
        maximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        overScroller = new OverScroller(getContext());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 直接调用系统的方法，把当前的宽高设定为子view的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isScrolling = false;
                mDownX = ev.getX();
                mDownScrolerX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = mDownX - ev.getX();
                Log.e(TAG, "dispatchTouchEvent: " + dx);
                if (!isScrolling) {
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        isScrolling = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;
                    }
                }
                break;
        }

        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                // 当前X的位置
                mDownScrolerX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 目标偏移量，按下坐标减去当前坐标，加上按下的坐标
                float dx = mDownX - event.getX() + mDownScrolerX;
                if (dx <= 0) {
                    dx = 0;
                }
                if (dx >= getWidth()) {
                    dx = getWidth();
                }
                scrollTo((int) dx, getScrollY());
                break;
            case MotionEvent.ACTION_UP:
                // 计算X轴的滑动
                velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
                // 获取当前手指滑动的距离
                float xVelocity = velocityTracker.getXVelocity();
                // 当前的X轴滚动的坐标
                int scrollX = getScrollX();
                // 目标的页面
                int targetPage;
                // 判断滑动的距离是否小于最小滑动距离
                if (Math.abs(xVelocity) < minimumFlingVelocity) {
                    // 大于宽度一半，则往下一页滑动
                    // 小于宽度一半，则滚回去
                    targetPage = scrollX > getWidth() / 2 ? 1 : 0;
                } else {
                    // 这里不是很明白
                    targetPage = xVelocity < 0 ? 1 : 0;
                }
                // 判断滑动的距离
                //
                int scrollDistance = targetPage == 1 ? (getWidth() - scrollX) : -scrollX;
                // 开始滚动
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                // 在每一帧执行一次
                postInvalidateOnAnimation();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        // 判断是否还有偏移
        if (overScroller.computeScrollOffset()) {
            // 滚到页面
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // 并排布局
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();
        int childCount = getChildCount();

        for (int index = 0; index < childCount; index++) {
            View childView = getChildAt(index);
            childView.layout(childLeft, childTop, childRight, childBottom);
            childLeft += getWidth();
            childRight += getWidth();
        }
    }
}
