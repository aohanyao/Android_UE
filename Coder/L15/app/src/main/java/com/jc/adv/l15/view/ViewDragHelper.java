package com.jc.adv.l15.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * view 拖拽
 */
public class ViewDragHelper extends ViewGroup implements View.OnDragListener {
    private final int ROW = 2;
    private final int COLUMN = 3;

    /**
     * 存放原始的子View的位置
     */
    private List<View> mChildViews = new LinkedList<>();

    public ViewDragHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    private View mDragView;

    private String TAG = getClass().getCanonicalName();


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
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View childAt = getChildAt(index);
            mChildViews.add(childAt);
            childAt.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mDragView = view;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(null, new DragShadowBuilder(view), view, 0);
                    }
                    return false;
                }
            });
            childAt.setOnDragListener(this);
        }
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

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (dragEvent.getLocalState() == view) {
                    view.setVisibility(View.INVISIBLE);
                }
                // 开始拖拽
                Log.e(TAG, "开始拖拽");
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                // 主要是感觉是在拖动中就会回调
//                Log.e(TAG, "主要是感觉是在拖动中就会回调");
                break;
            case DragEvent.ACTION_DROP:
                // 向用户已释放拖动阴影的视图发出信号，拖动点位于视图的边界框内，而不在可接受数据的后代视图中。
                Log.e(TAG, "向用户已释放拖动阴影的视图发出信号，拖动点位于视图的边界框内，而不在可接受数据的后代视图中。");
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                // 拖拽已结束
                Log.e(TAG, "拖拽已结束");
                if (dragEvent.getLocalState() == view) {
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                // 拖拽的View已经到了其他View的范围，判断点为手指触摸的位置
                if (dragEvent.getLocalState() != view) {
                    // 每个View都会收到的，所以要排除掉自己
                    sortView(view);
                    Log.e(TAG, "拖拽的View已经到了其他View的范围");

                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // 移出了当前View的范围，判断点为手指触摸的位置
                Log.e(TAG, "移出了当前View的范围");
                break;
        }
        return true;
    }

    /**
     * 开始对View进行排序
     *
     * @param targetView
     */
    private void sortView(View targetView) {
        int dragIndex = -1;
        int targetIndex = -1;
        int size = mChildViews.size();
        for (int i = 0; i < size; i++) {
            View childView = mChildViews.get(i);
            if (childView == mDragView) {
                dragIndex = i;
            }
            if (childView == targetView) {
                targetIndex = i;
            }

        }
        if (dragIndex == -1 || targetIndex == -1) return;

        float targetTranslationX = targetView.getTranslationX();
        float targetTranslationY = targetView.getTranslationY();

        List<View> animateViews = new LinkedList<>();
        animateViews.addAll(mChildViews);

        // 计算出来了当前移动的位置和我要移动的位置，
        // 现在该开始进行每个View的动画
        if (dragIndex > targetIndex) {
            // 往前移动  5->3
            // 那么是从
            // 3->4
            // 4->5 这样移动的
            for (int index = targetIndex; index < dragIndex; index++) {
                Log.e(TAG, "sortView: " + index);
                // 当前View
                View childView = mChildViews.get(index);
                // 下一个View
                View childViewNext = mChildViews.get(index + 1);
                childView.animate()
                        .translationX(childViewNext.getTranslationX())
                        .translationY(childViewNext.getTranslationY())
                        .setDuration(300)
                        .start();
                // 虽然移动了，但是view的下标集合没有进行更改
            }

        } else {
            // 往后移动  3->5
            // 那么是从
            // 5->4
            // 4->3 这样移动的
            for (int i = dragIndex + 1; i < targetIndex; i++) {
                View childView = mChildViews.get(i);

            }
        }

        mChildViews.get(dragIndex).setTranslationX(targetTranslationX);
        mChildViews.get(dragIndex).setTranslationY(targetTranslationY);

    }
}
