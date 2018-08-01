package me.limeice.android.g2d;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@TargetApi(23)
public class GameLiteViewV23 extends SurfaceView implements GameLiteView,
        GameLiteView.OnDrawFrame, GameLiteView.TouchEventListener, SurfaceHolder.Callback {

    @NonNull
    protected GameLiteView.OnDrawFrame onDraw = this;
    @NonNull
    protected TouchEventListener mTouchEventListener = this;

    private int fps = 0;
    private int mDelay = 33;
    private boolean isEnableTouchEvent = false;
    private final ProxyThread mProxy = new ProxyThread(this);
    private boolean isSurfaceCreated = false;
    private boolean requestStart = false;


    public GameLiteViewV23(Context context) {
        this(context, null);
    }

    public GameLiteViewV23(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLiteViewV23(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化（用于类内部，请勿调用）
     *
     * @param context 上下文
     * @param attrs   属性
     */
    @Override
    public void init(Context context, @Nullable AttributeSet attrs) {
        // 注册SurfaceHolder的回调方法
        getHolder().addCallback(this);
    }

    /**
     * 刷新，可运行在非UI线程
     */
    @Override
    public synchronized void postInvalidateCompat() {
        final Surface surface = getHolder().getSurface();
        Canvas canvas = surface.lockHardwareCanvas();
        if (canvas == null)
            return;
        onDraw.onDrawFrame(canvas);
        surface.unlockCanvasAndPost(canvas);
    }

    /**
     * 获得FPS
     *
     * @return FPS
     */
    @Override
    public int getFPS() {
        return fps;
    }

    /**
     * 自动刷新开启时生效(0代表手动刷新)
     *
     * @param fps FPS (0 ~ 60)
     */
    @Override
    public void setFPS(int fps) {
        if (fps < 0 || fps > 60)
            throw new IllegalArgumentException("fps range: 0 ~ 60.");
        this.fps = fps;
        if (fps > 0)
            this.mDelay = 1000 / fps;
    }

    /**
     * 自动刷新，当 {@link #getFPS()} 为 1~60 才有效
     */
    @Override
    public void startAutoRefresh() {
        if (isSurfaceCreated)
            mProxy.start();
        else {
            requestStart = true;
        }
    }

    /**
     * 关闭自动刷新
     */
    @Override
    public void stopAutoRefresh() {
        mProxy.stop();
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        mProxy.stop();
    }

    /**
     * 设置作画
     *
     * @param drawFrame {@link OnDrawFrame}
     */
    @Override
    public void setOnDraw(@NonNull OnDrawFrame drawFrame) {
        Objects.requireNonNull(drawFrame, "The OnDrawFrame can not be empty.");
        onDraw = drawFrame;
    }

    /**
     * 设置触摸事件监听
     *
     * @param listener {@link TouchEventListener}
     */
    @Override
    public void setOnTouchEventListener(@NonNull TouchEventListener listener) {
        Objects.requireNonNull(listener, "The TouchEventListener can not be empty.");
        mTouchEventListener = listener;
    }

    /**
     * 获取触摸事件监听是否开启
     *
     * @return 获取触摸事件监听是否开启
     */
    @Override
    public boolean isEnableTouchEvent() {
        return isEnableTouchEvent;
    }

    /**
     * 设置触摸监听是否启用
     *
     * @param enable 启用与否
     */
    @Override
    public void setOnTouchEventEnable(boolean enable) {
        isEnableTouchEvent = enable;
    }

    /**
     * 获得GameView的View对象
     *
     * @return {@link GameLiteViewV16} or {@link GameLiteViewV23}
     */
    @NonNull
    @Override
    public View getHolderView() {
        return this;
    }

    /**
     * 获得帧率延迟 1000 / FPS
     *
     * @return 帧率延迟
     */
    @Override
    public int getDelay() {
        return mDelay;
    }

    /**
     * 作画
     *
     * @param canvas 画布
     */
    @Override
    public void onDrawFrame(@NonNull Canvas canvas) {
        // Nothing
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableTouchEvent || mTouchEventListener == this)
            return super.onTouchEvent(event);
        return mTouchEventListener.onTouchEvent(event);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceCreated = true;
        if (requestStart) {
            mProxy.start();
            requestStart = false;
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreated = false;
        mProxy.stop();
    }
}