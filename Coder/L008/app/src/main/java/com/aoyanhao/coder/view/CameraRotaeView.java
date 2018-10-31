package com.aoyanhao.coder.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.aoyanhao.R;
import com.aoyanhao.coder.utils.Utils;

/**
 * Created by 江俊超 on 2018/10/31.
 * Version:1.0
 * Description: 旋转图
 * ChangeLog:
 */
public class CameraRotaeView extends View {

    private final String TAG = "ImageTextView";
    /**
     * 画笔
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private Camera mCamera;
    private float degrees = 20;

    private Animator animator;
    /**
     * 图片的宽度
     */
    private final float BITMAP_WIDTH = Utils.dp2px(200);

    {
        mPaint.setTextSize(Utils.dp2px(20));
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, BITMAP_WIDTH);
        mCamera = new Camera();
        mCamera.rotateX(45);
        mCamera.setLocation(0, 0, -8 * getResources().getDisplayMetrics().density);
    }

    public CameraRotaeView(Context context) {
        super(context);
    }

    public CameraRotaeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraRotaeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float centerX = BITMAP_WIDTH;
        float centerY = BITMAP_WIDTH;

        // ----绘制上半部分
        canvas.save();
        canvas.translate(centerX, centerX);
        canvas.rotate(-degrees);
        canvas.clipRect(-BITMAP_WIDTH, -BITMAP_WIDTH, BITMAP_WIDTH, 0);
        canvas.rotate(degrees);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(mBitmap, centerX - BITMAP_WIDTH / 2, centerY - BITMAP_WIDTH / 2, mPaint);
        canvas.restore();


        // 绘制下半部分
        canvas.save();
        // 其实是把 x0 y0 移动到了中心点
        // canvas.translate 移动的是坐标原点，而不是画布
        canvas.translate(centerX, centerY);
        canvas.rotate(-degrees);
        // 然后将重心点移动到原点附近
//        if (degrees != 0)
            mCamera.applyToCanvas(canvas);
        canvas.clipRect(-BITMAP_WIDTH, 0, BITMAP_WIDTH, BITMAP_WIDTH);
        canvas.rotate(degrees);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(mBitmap, centerX - BITMAP_WIDTH / 2, centerY - BITMAP_WIDTH / 2, mPaint);
        canvas.restore();
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
        invalidate();
    }

    /**
     * 应该使用更多的动画来完善
     */
    @Deprecated
    public void startAmin() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        animator = ObjectAnimator.ofFloat(this, "degrees", 20, 360);
        animator.setDuration(3500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                setDegrees(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


}
