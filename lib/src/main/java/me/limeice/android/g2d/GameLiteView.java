package me.limeice.android.g2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEventSource;

public interface GameLiteView extends Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {

    /**
     * 初始化（用于类内部，请勿调用）
     *
     * @param context 上下文
     * @param attrs   属性
     */
    void init(Context context, @Nullable AttributeSet attrs);

    /**
     * 刷新，可运行在非UI线程
     */
    void postInvalidateCompat();

    /**
     * 获得FPS
     *
     * @return FPS
     */
    int getFPS();

    /**
     * 自动刷新开启时生效(0代表手动刷新)
     *
     * @param fps FPS (0 ~ 60)
     */
    void setFPS(@IntRange(from = 0, to = 60) int fps);

    /**
     * 设置作画
     *
     * @param drawFrame {@link OnDrawFrame}
     */
    void setOnDraw(@NonNull final OnDrawFrame drawFrame);

    /**
     * 设置触摸事件监听
     *
     * @param listener {@link TouchEventListener}
     */
    void setOnTouchEventListener(@NonNull final TouchEventListener listener);

    /**
     * 获取触摸事件监听是否开启
     *
     * @return 获取触摸事件监听是否开启
     */
    boolean isEnableTouchEvent();

    /**
     * 设置触摸监听是否启用
     *
     * @param enable 启用与否
     */
    void setOnTouchEventEnable(boolean enable);

    /**
     * 获得GameView的View对象
     *
     * @return {@link GameLiteViewV16} or {@link GameLiteViewV23}
     */
    @NonNull
    View getHolderView();

    /**
     * 自动刷新，当 {@link #getFPS()} 为 1~60 才有效
     */
    void startAutoRefresh();

    /**
     * 关闭自动刷新
     */
    void stopAutoRefresh();

    /**
     * 销毁
     */
    void onDestroy();

    /**
     * 获得帧率延迟 1000 / FPS
     *
     * @return 帧率延迟
     */
    int getDelay();

    /**
     * 作画
     */
    interface OnDrawFrame {

        /**
         * 作画
         *
         * @param canvas 画布
         */
        void onDrawFrame(@NonNull Canvas canvas);
    }

    /**
     * 触摸事件监听
     */
    interface TouchEventListener {

        /**
         * 触摸事件监听
         *
         * @param event 事件
         * @return 是否拦截
         */
        boolean onTouchEvent(MotionEvent event);
    }
}
