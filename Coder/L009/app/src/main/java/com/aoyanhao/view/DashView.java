package com.aoyanhao.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.aoyanhao.R;
import com.aoyanhao.utils.Utils;

/**
 * Created by 江俊超 on 2018/11/3.
 * Version:1.0
 * Description: 转动的图片
 * ChangeLog:
 */
public class DashView extends View {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float BITMAP_SIZE = Utils.dp2px(200);

    private Camera mCamera;

    private Bitmap mBitmap;
    private float mCenterY;
    private float mCenterX;

    private float degrees = 0;

    private float degX = 0;

    private float degY = 0;

    private AnimatorSet animatorSet;

    public DashView(Context context) {
        super(context);
    }

    public DashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, BITMAP_SIZE);
        mCamera = new Camera();
        mCamera.setLocation(0, 0, -8 * getResources().getDisplayMetrics().density);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = BITMAP_SIZE;
        mCenterY = BITMAP_SIZE;
//        mCenterX = getWidth() / 2 - BITMAP_SIZE / 2;
//        mCenterY = getHeight() / 2 - BITMAP_SIZE / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffcccccc);
        //-------------------------绘制上半部分 start
        canvas.save();
        // 将原点移动到中心去
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(-degrees);
        // 进行裁剪，现在是居中了，所以底部就是0，顶部是负数
        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0);
        mCamera.save();
        mCamera.rotateX(-degY);
        mCamera.applyToCanvas(canvas);
        canvas.rotate(degrees);
        // 将原点移动回到0.0
        canvas.translate(-mCenterX, -mCenterY);
        // 绘制 图片
        canvas.drawBitmap(mBitmap, mCenterX - BITMAP_SIZE / 2, mCenterY - BITMAP_SIZE / 2, mPaint);

        canvas.restore();
        mCamera.restore();

        //-------------------------绘制上半部分 end


        //-------------------------绘制下半部分 start
        canvas.save();
        // 将原点移动到中心
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(-degrees);
        // 进行裁剪
        canvas.clipRect(-BITMAP_SIZE, 0, BITMAP_SIZE, BITMAP_SIZE);
        mCamera.save();
        mCamera.rotateX(degX);
        mCamera.applyToCanvas(canvas);
        canvas.rotate(degrees);
        // 将原点移动回到 0,0
        canvas.translate(-mCenterX, -mCenterX);
        // 绘制bitmap
        canvas.drawBitmap(mBitmap, mCenterX - BITMAP_SIZE / 2, mCenterY - BITMAP_SIZE / 2, mPaint);
        canvas.restore();
        mCamera.restore();
        //-------------------------绘制下半部分 end
    }


    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
        postInvalidate();
    }

    public float getDegX() {
        return degX;
    }

    public void setDegX(float degX) {
        this.degX = degX;
        postInvalidate();
    }


    public float getDegY() {
        return degY;
    }

    public void setDegY(float degY) {
        this.degY = degY;
        postInvalidate();
    }

    public void start() {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }
        // 恢复初始状态
        setDegrees(0);
        setDegX(0);
        setDegY(0);

        // 先旋转X轴，然后进行转动，最后进行Y轴选准
        // 翻页动画
        ObjectAnimator degXAnimator = ObjectAnimator.ofFloat(this, "degX", 0, 45);
        degXAnimator.setDuration(1200);


        // 整个旋转动画
        ObjectAnimator degreesAnimator = ObjectAnimator.ofFloat(this, "degrees", 0, 270);
        degreesAnimator.setDuration(3000);

        ObjectAnimator degYAnimator = ObjectAnimator.ofFloat(this, "degY", 0, 45);
        degYAnimator.setDuration(1200);


        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(degXAnimator, degreesAnimator, degYAnimator);
        animatorSet.start();
    }
}
