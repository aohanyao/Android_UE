package com.aoyanhao.coder.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aoyanhao.R;
import com.aoyanhao.coder.utils.Utils;

/**
 * 未完成
 */
@Deprecated
public class ImageTextView extends View {
    private final String TAG = "ImageTextView";
    /**
     * 画笔
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    /**
     * 图片的宽度
     */
    private final float BITMAP_WIDTH = Utils.dp2px(100);

//    private final String mText = "共产主义（Communism）是一种政治观点和思想体系，现今的共产主义奉马克思、恩格斯思想为基本思想。共产主义主张消灭生产资料私有制，并建立一个没有阶级制度、没有剥削、没有压迫，实现人类自我解放的社会，也是社会化集体大生产的社会，面对恶势力也会团结一致。共产主义者认为未来所有阶级社会最终将过渡到各尽所能 各取所需的共产主义社会，人类社会的意识形态将进入高级阶段 [1]  。";
    private final String mText = "Stell dir eine Welt vor, in der jeder einzelne Mensch frei an der Summe allen Wissens teilhaben kann.The non-profit Wikimedia Foundation provides the essential infrastructure for free knowledge. We host Wikipedia, the free online encyclopedia, created and edited by volunteers around the world, as well as many other vital community projects. We welcome anyone who shares our vision to join us in collecting and sharing knowledge that fully represents human diversity.";

    // 测量文字的
    private float[] mTextRectf = new float[1];

    private Paint.FontMetrics fontMetrics;

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mPaint.setTextSize(Utils.dp2px(20));
        mTextPaint.setTextSize(Utils.dp2px(20));
//        mPaint.setTextAlign(Paint.Align.LEFT);
        mBitmap = Utils.getDrawableBitmap(getContext(), R.drawable.icon_android_road, BITMAP_WIDTH);
        fontMetrics = new Paint.FontMetrics();
        mPaint.getFontMetrics(fontMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawColor(0xffefefef);

        // 先绘制图片
        canvas.drawBitmap(mBitmap, getWidth() - BITMAP_WIDTH, BITMAP_WIDTH / 2, mPaint);

        int originalBreakTextIndex = 0;
        int originalBreakTextLine = 1;
        // 没绘制到最后一个，继续
        while (originalBreakTextIndex < mText.length()) {
            // 文字的Y轴
            float mTextY = originalBreakTextLine * mPaint.getFontSpacing();
            // 文字测量的宽度
            float measureMaxWidth = getWidth() - Utils.dp2px(20);
            //---------------------如何计算 with
            // 最大y点，和最小y点 5 是偏移量，写死的
            if (mTextY > (BITMAP_WIDTH / 2)/* - 5*/ && mTextY < (BITMAP_WIDTH / 2) + BITMAP_WIDTH /*+ 5*/) {
                measureMaxWidth = getWidth() - BITMAP_WIDTH- Utils.dp2px(20);
//                Log.e("onDraw: ", "BITMAP_WIDTH:" + BITMAP_WIDTH+"      getWidth():"+getWidth()+"          measureMaxWidth:"+measureMaxWidth);
            }
            //---------------------如何计算 with


            // 测量文字
            int newBreakTextIndex = mPaint.breakText(mText,
                    originalBreakTextIndex,
                    mText.length(),
                    false,
                    measureMaxWidth,
                    mTextRectf);
//            Log.e(TAG, "originalBreakTextIndex:" + originalBreakTextIndex + "    newBreakTextIndex:" + newBreakTextIndex);

            // 绘制文字
            canvas.drawText(mText,
                    originalBreakTextIndex,
                    originalBreakTextIndex + newBreakTextIndex,
                    0,
                    mTextY,
                    mTextPaint);
            Log.e(TAG, "onDraw: " + mText.substring(originalBreakTextIndex, originalBreakTextIndex + newBreakTextIndex));

            originalBreakTextLine++;
            // 下标相加
            originalBreakTextIndex += newBreakTextIndex;
        }


    }
}
