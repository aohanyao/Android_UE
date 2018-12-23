package com.jc.adv.l14.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
public class TowPagerViewPager2 extends ViewGroup {
    private VelocityTracker velocityTracker;
    private float mDownX;

    private boolean isScroller = false;
    private String TAG = getClass().getSimpleName();
    private ViewConfiguration viewConfiguration;
    private float mDownScrollX;
    private OverScroller overScroller;

    public TowPagerViewPager2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        velocityTracker = VelocityTracker.obtain();
        viewConfiguration = ViewConfiguration.get(getContext());
        overScroller = new OverScroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1. 测量子view
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean b, int right, int top, int left, int bottom) {
        // 2. 布局子view
        int childCount = getChildCount();
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();

        for (int index = 0; index < childCount; index++) {
            View childAt = getChildAt(index);
            childAt.layout(childLeft, childTop, childRight, childBottom);
            childLeft += getWidth();
            childRight += getWidth();
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 3.拦截事件

        // 3.1 清理速度追踪器
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        // 对事件进行处理

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                isScroller = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 两个点之间的差值
                float dx = mDownX - ev.getX();
                if (!isScroller) {
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        isScroller = true;
                        // 请求父view不拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 4. 处理拦截事件
        // 4.1 清理速度追踪器
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 按下的位置减去当前的位置，得到偏移，然后加上原始的位置就等于当前
                // 的位置
                float targetScrollX = mDownX - event.getX() + mDownScrollX;
                // 边界判断
                // 向右(-->)不能滑动超过最左边小于0。
                // 向左(<--)不能滑动超过getWidth()的宽度。
                targetScrollX = Math.max(0, targetScrollX);
                targetScrollX = Math.min(getWidth(), targetScrollX);
                // 进行滚动
                scrollTo((int) targetScrollX, getScrollY());
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起，计算滑动速度
                velocityTracker.computeCurrentVelocity(1000, viewConfiguration.getScaledMaximumFlingVelocity());
                // 当前X轴的滑动速度
                float xVelocity = velocityTracker.getXVelocity();
                // 当前X轴的坐标
                float currentScrollX = getScrollX();
                int targetPage;
                // 小于最小滑动速度，滑动得特别小,那么就不认为这是一次快速滑动
                if (Math.abs(xVelocity) < viewConfiguration.getScaledMinimumFlingVelocity()) {
                    // 已经滑动大于中心点了，意思就是说想要滑动到第1页
                    if (currentScrollX > getWidth() / 2) {
                        targetPage = 1;
                    } else {
                        // 没有滑动超过中心点，那么就是回到第0页
                        targetPage = 0;
                    }
                } else {
                    // X轴的滑动速度，这里还是不明白
                    // 滑动速度 小于0，为什么小于0就是第一页
                    // 负数就是向左滑动，正数就是向右滑动
                    if (xVelocity < 0) {
                        targetPage = 1;
                    } else {
                        targetPage = 0;
                    }
                    // 可以通过velocityTracker.getXVelocity()的值来判断滑动的方向
                    // 他返回的是一个滑动速度，有可能是正数，也有可能是负数，如果是正数，
                    // 则是向右滑动-->,如果是负数，则是向左滑动<--。所以要进行一次绝对
                    // 值的判断，判断是否小于最小滑动速度，如果是则代表着不算是一次快速
                    // 滑动。
                }
                // 应该滚动多少
                int targetX;
                if (targetPage == 1) {
                    // 滚动到 getWidth（）
                    targetX = (int) (getWidth() - currentScrollX);
                } else {
                    // 滚回到0，所以是负数的当前X的位置
                    targetX = (int) -currentScrollX;
                }
                // overScroller主要用来做滑动，给一个开始坐标和一个结束坐标，然后我们不
                // 断的取问overScroller拿当前的坐标来进行使用。
                overScroller.startScroll(getScrollX(), 0, targetX, 0);
                // 在下一帧绘制的时候调用computeScroll
                postInvalidateOnAnimation();
                break;

        }

        return true;
    }

    @Override
    public void computeScroll() {
        // 计算和判断当前有没有滚动完成
        if (overScroller.computeScrollOffset()) {
            // 获取到当前的X和Y坐标
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            // 在下一帧继续调用
            postInvalidateOnAnimation();
        }
    }
}
