package com.aohanao.coder.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aohanao.coder.util.Utils;

public class PieChatView extends View {
    private static final int OUT_INDEX = 2;
    private static final float PADDING = Utils.dp2px(15);
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRect;

    // 半径
    private final float RADIUS = Utils.dp2px(150);

    private final int[] ANGLES = {60, 100, 120, 80};
    private final int[] COLORS = {Color.parseColor("#2079ff")
            , Color.parseColor("#c2185b")
            , Color.parseColor("#009688")
            , Color.parseColor("#ff8f00")};

    public PieChatView(Context context) {
        super(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS);
    }

    public PieChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentAngle = 0;

        for (int i = 0; i < ANGLES.length; i++) {
            mPaint.setColor(COLORS[i]);
            canvas.save();

            if (i == OUT_INDEX) {
                canvas.translate((float) Math.cos(Math.toRadians(currentAngle + ANGLES[i] / 2)) * PADDING,
                        (float) Math.sin(Math.toRadians(currentAngle + ANGLES[i] / 2)) * PADDING);
            }

            canvas.drawArc(mRect,
                    currentAngle,
                    ANGLES[i],
                    true,
                    mPaint);
            currentAngle += ANGLES[i];
            canvas.restore();
        }
    }
}
