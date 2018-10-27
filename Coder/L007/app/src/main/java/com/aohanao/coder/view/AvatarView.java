package com.aohanao.coder.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aohanao.coder.R;
import com.aohanao.coder.util.Utils;

public class AvatarView extends View {
    private Bitmap bitmap;

    private static final float PADDING = Utils.dp2px(15);
    private static final float SRTOK_WIDTH = Utils.dp2px(5);
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float width = Utils.dp2px(300);
    RectF rectF;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {

        bitmap = Utils.getDrawableBitmap(getContext(),
                R.drawable.icon_android_road,
                width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(PADDING, PADDING, width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(rectF, mPaint);

        int saveLayerCount = canvas.saveLayer(PADDING, PADDING, width, width, mPaint);
        canvas.drawOval(PADDING + SRTOK_WIDTH,
                PADDING + SRTOK_WIDTH,
                width - SRTOK_WIDTH,
                width - SRTOK_WIDTH,
                mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap,
                PADDING,
                PADDING,
                mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayerCount);
    }
}
