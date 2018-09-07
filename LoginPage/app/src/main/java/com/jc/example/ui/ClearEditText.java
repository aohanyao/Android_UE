package com.jc.example.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.jc.example.R;
import com.jc.example.utils.DensityUtils;
import com.jc.example.utils.KeyBoardUtils;


/**
 * 自带清除按钮的输入框
 * <p>焦点获取，显示清除按钮</p>
 */
public class ClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 临时的删除引用 占位
     */
    private Drawable mTempDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;
    /**
     * 类型
     */
    private int type = Type.NONE;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
//        	throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.delete_edit_text_icon);
        }
        mTempDrawable = getResources().getDrawable(R.drawable.delete_edit_text_icon);

        mClearDrawable.setBounds(0, 0, DensityUtils.dp2px(getContext(), 18), DensityUtils.dp2px(getContext(), 18));
        mTempDrawable.setBounds(0, 0, DensityUtils.dp2px(getContext(), 18), DensityUtils.dp2px(getContext(), 18));
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null /*&& getCompoundDrawables()[2] == mClearDrawable*/) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
//        if (hasFocus) {
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        KeyBoardUtils.foucsString(this);
        setClearIconVisible(getText().length() > 0&&hasFocus);
//        } else {
//            setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
//            setClearIconVisible(false);
//        }
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
//    	this.setAnimation();
        this.startAnimation(shakeAnimation(5));
    }


    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(800);
        return translateAnimation;
    }


    public interface Type {
        /**
         * 无
         */
        int NONE = 1;

    }
}
