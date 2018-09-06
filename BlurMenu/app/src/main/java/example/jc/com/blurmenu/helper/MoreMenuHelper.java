package example.jc.com.blurmenu.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RelativeLayout;

import example.jc.com.blurmenu.R;
import example.jc.com.blurmenu.base.BaseAnimatorListener;
import example.jc.com.blurmenu.utils.FastBlur;
import example.jc.com.blurmenu.utils.ScreenUtils;


/**
 * Created by jc on 2017/4/13 0013.
 * <p>版本:1.0.0</p>
 * <b>说明<b>首页的更多菜单帮助类<br/>
 * <li></li>
 */
public class MoreMenuHelper {
    private Activity mActivity;
    RelativeLayout rlMoreMenuRoot;
    private Bitmap overlay;
    private Bitmap mBitmap;
    private Handler mHandler = new Handler();
    FloatingActionButton fabCloseMoreMenu;
    private final RelativeLayout rlMenuWrap;

    public MoreMenuHelper(Activity mActivity, RelativeLayout rlMenuHome) {
        this.mActivity = mActivity;
        this.rlMoreMenuRoot = rlMenuHome;
        fabCloseMoreMenu = (FloatingActionButton) rlMoreMenuRoot.findViewById(R.id.fab_close_more_menu);
        rlMenuWrap = (RelativeLayout) this.rlMoreMenuRoot.findViewById(R.id.rl_menu_warp);
        initEvent();
    }

    private void initEvent() {
        rlMoreMenuRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMoreMenu(null);
            }
        });
    }

    /**
     * 显示更多窗口的
     */
    public void showMoreWindow() {
        rlMoreMenuRoot.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), blur()));
        rlMoreMenuRoot.setVisibility(View.VISIBLE);
        rotationActionMenu(45, 180);
        showAnimation();
    }

    /**
     * 获得高斯模糊的背景
     *
     * @return
     */
    private Bitmap blur() {
//        if (null != overlay) {
//            overlay.recycle();
//            overlay = null;
//            System.gc();
//        }
        long startMs = System.currentTimeMillis();

        mBitmap = ScreenUtils.snapShotWithStatusBar(mActivity);

        float scaleFactor = 8;//图片缩放比例；
        float radius = 3;//模糊程度
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        try {
            overlay = FastBlur.doBlur(overlay, (int) radius, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return overlay;
    }

    /**
     * 关闭更多窗口
     *
     * @param newActionActivityClass
     */
    public void closeMoreMenu(String newActionActivityClass) {
        closeAnimation(newActionActivityClass);
        rotationActionMenu(180, 45);
    }

    /**
     * 打开动画
     */
    private void showAnimation() {
        for (int i = 0; i < rlMenuWrap.getChildCount(); i++) {
            final View child = rlMenuWrap.getChildAt(i);
            if (child.getId() == R.id.fab_close_more_menu || child.getId() == R.id.rl_bg) {
                continue;
            }
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 60);
        }

    }

    /**
     * 旋转菜单按钮
     */
    private void rotationActionMenu(int from, int to) {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(fabCloseMoreMenu, "rotation", from, to);
        fadeAnim.setDuration(300);
        KickBackAnimator kickAnimator = new KickBackAnimator();
        kickAnimator.setDuration(150);
        fadeAnim.setEvaluator(kickAnimator);
        fadeAnim.start();
    }

    /**
     * 关闭动画
     *
     * @param newActionActivityClass
     */
    private void closeAnimation(String newActionActivityClass) {
        for (int i = 0; i < rlMenuWrap.getChildCount(); i++) {
            final View child = rlMenuWrap.getChildAt(i);
            if (child.getId() == R.id.fab_close_more_menu || child.getId() == R.id.rl_bg) {
                continue;
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new BaseAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }, (rlMenuWrap.getChildCount() - i - 1) * 30);

            //从第0个开始
            if (i == 0) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        rlMoreMenuRoot.setVisibility(View.GONE);
                        onDestroy();
                        //打开界面

                    }
                }, (rlMenuWrap.getChildCount() - i) * 30 + 80);
            }
        }

    }


    public void onDestroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }
}
