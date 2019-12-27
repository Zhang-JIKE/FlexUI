package com.daily.flexui.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.daily.flexui.interpolator.BezierInterpolator;

//可以跟着手指右滑移动的Activity
public class SwipeActivity extends FragmentActivity {


    /** 框架视图 */
    protected SlideFrame frameView;
    /** 内容视图 */
    protected View contentView;

    @Override
    public void setContentView(int layoutResID) {
        // 初始化frame
        if (frameView == null) {
            // 未初始化则初始化
            frameView = new SlideFrame(this);
        } else {
            // 已经初始化则清空
            frameView.removeAllViews();
        }
        // 创造framelayout的填充参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        // 获取layoutResId对应的contentView视图并插入frameView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(layoutResID, null);
        frameView.addView(contentView, params);
        // 设置frameview为根视图
        super.setContentView(frameView);
    }

    @Override
    public void setContentView(View view) {
        // 初始化frame
        if (frameView == null) {
            // 未初始化则初始化
            frameView = new SlideFrame(this);
        } else {
            // 已经初始化则清空
            frameView.removeAllViews();
        }
        // 创造framelayout的填充参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        // 获取view为contentView视图并插入frameView
        contentView = view;
        frameView.addView(contentView, params);
        // 设置frameview为根视图
        super.setContentView(frameView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        // 初始化frame
        if (frameView == null) {
            // 未初始化则初始化
            frameView = new SlideFrame(this);
        } else {
            // 已经初始化则清空
            frameView.removeAllViews();
        }
        // 创造framelayout的填充参数
        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(-1, -1);
        // 获取view为contentView视图并插入frameView
        contentView = view;
        frameView.addView(contentView, fp);
        // 设置frameview为根视图
        super.setContentView(frameView, params);
    }

    /**
     * 推出页面
     */
    protected void onSlideFinish() {
        finish();
    }

    /**
     * 位移内容视图到
     *
     * @param position
     *            目标位置
     */
    public void slideTo(int position) {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            contentView.setY(position);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentView
                    .getLayoutParams();
            params.setMargins(0, -position, 0, position);
            contentView.setLayoutParams(params);
        }
    }

    /**
     * 获得当前容器位移
     *
     * @return 当前容器位移
     */
    public int getSlide() {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            return (int) contentView.getY();
        } else {
            return ((FrameLayout.LayoutParams) contentView
                    .getLayoutParams()).bottomMargin;
        }
    }

    public class SlideFrame extends FrameLayout {
        /** 默认滑动阀值 */
        private final static int DEFAULT_SLIDE_DUMPING = 8;
        /** 默认状态改变阀值 */
        private final static int DEFAULT_DO_DUMPING = 100;
        /** 滑动起始位置与当前位置 */
        private int startX, currentX, startY, currentY;
        /** 是否拦截事件，是否已经完成滑动检查 */
        private boolean doNotIntercept, hasChecked;
        /** 滑动阀值 */
        private int slideDumping;
        /** 操作阀值 */
        private int doDumping;
        /** 滑屏动画 */
        protected SlideAnimation slideAnimation;

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            super.onInterceptTouchEvent(ev);
            // 若当前处在侧滑状态中，则拦截信号
            if ((!doNotIntercept) && hasChecked) {
                return true;
            }
            // 否则使用默认
            return false;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 获得起始滑动坐标
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                // 初始化状态
                doNotIntercept = false;
                hasChecked = false;
            } else if (!doNotIntercept) {
                // 获得当前滑动坐标
                currentX = (int) ev.getX();
                currentY = (int) ev.getY();
                // 根据滑动类型区分
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_MOVE: // 移动状态
                        if (hasChecked) {
                            doSlide();
                        } else {
                            doCheck();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL: // 取消状态
                    case MotionEvent.ACTION_UP: // 抬起状态
                        // 初始化状态
                        doNotIntercept = false;
                        hasChecked = false;
                        if (Math.abs(currentY - startY) > doDumping) {
                            if (currentY < startY) {
                                // 上滑
                                slideAnimation = new SlideAnimation(getSlide(),
                                        -contentView.getHeight(), 0);
                                slideAnimation
                                        .setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(
                                                    Animation animation) {
                                            }

                                            @Override
                                            public void onAnimationRepeat(
                                                    Animation animation) {
                                            }

                                            @Override
                                            public void onAnimationEnd(
                                                    Animation animation) {
                                                onSlideFinish();
                                            }
                                        });
                                startAnimation(slideAnimation);
                            }
                        } else {
                            // 返回0位置
                            slideAnimation = new SlideAnimation(getSlide(), 0, 0);
                            startAnimation(slideAnimation);
                        }
                        break;
                    default:
                        break;
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        /**
         * 检查是否超过滑动阀值开启滑动状态
         */
        private void doCheck() {
            if (Math.abs(startX - currentX) > slideDumping) {
                hasChecked = true;
                doNotIntercept = true;
                slideTo(0);
            } else if(startY - currentY > slideDumping){
                hasChecked = true;
                doNotIntercept = false;
            }
        }

        /**
         * 进行滑动
         */
        private void doSlide() {
            if (currentY < startY) {
                slideTo(currentY - startY);
            } else {
                slideTo(0);
            }
        }

        /**
         * 设置滑动阀值
         *
         * @param dpValue
         */
        public void setSlideDumping(int dpValue) {
            slideDumping = dip2px(dpValue);
        }

        /**
         * 设置状态改变阀值
         *
         * @param dpValue
         */
        public void setDoDumping(int dpValue) {
            doDumping = dip2px(dpValue);
        }

        /**
         * 二级构造方法
         */
        private void initilize() {
            setSlideDumping(DEFAULT_SLIDE_DUMPING);
            setDoDumping(DEFAULT_DO_DUMPING);
            doNotIntercept = false;
            hasChecked = false;
            setClickable(true);
            setFocusable(true);
            setFocusableInTouchMode(true);
        }

        /**
         * 构造方法
         *
         * @param context
         * @param attrs
         * @param defStyle
         */
        public SlideFrame(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initilize();
        }

        /**
         * 构造方法
         *
         * @param context
         * @param attrs
         */
        public SlideFrame(Context context, AttributeSet attrs) {
            super(context, attrs);
            initilize();
        }

        /**
         * 构造方法
         *
         * @param context
         */
        public SlideFrame(Context context) {
            super(context);
            initilize();
        }

        /**
         * 讲dip值转换为px值，像素密度距离转像素距离
         *
         * @param dipValue dp值
         * @return px值
         */
        private int dip2px(float dipValue) {
            // 获得像素密度
            final float scale = getContext().getResources().getDisplayMetrics().density;
            // 四舍五入dp值乘像素密度
            return (int) (dipValue * scale + 0.5f);
        }
    }

    /**
     * 滑动动画类
     *
     * @author HalfmanG2
     */
    public class SlideAnimation extends Animation {
        /** 起始位置，目标位置 */
        private float from, to;
        /**
         * 构造方法
         * @param from 起始位置
         * @param to 目标位置
         * @param startOffset 起始延迟
         */
        public SlideAnimation(int from, int to, int startOffset) {
            this.from = from;
            this.to = to;
            setFillEnabled(false);
            setDuration(260);
            setRepeatCount(0);
            setStartOffset(startOffset);
            setInterpolator(new BezierInterpolator(0.282f,0.451f,0.352f,0.9f));
        }
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            float current = from + (to - from) * interpolatedTime;
            slideTo((int) current);
            super.applyTransformation(interpolatedTime, t);
        }
    }

}
