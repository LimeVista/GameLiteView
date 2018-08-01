package me.limeice.android.g2d;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@TargetApi(16)
public class GameLiteViewV16 extends View implements GameLiteView,
        GameLiteView.OnDrawFrame, GameLiteView.TouchEventListener {

    @NonNull
    protected GameLiteView.OnDrawFrame onDraw = this;
    @NonNull
    protected TouchEventListener mTouchEventListener = this;

    private int fps = 0;
    private int mDelay = 33;
    private boolean isEnableTouchEvent = false;
    private final ProxyThread mProxy = new ProxyThread(this);

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public GameLiteViewV16(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #GameLiteViewV16(Context, AttributeSet, int)
     */
    public GameLiteViewV16(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public GameLiteViewV16(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    }

    /**
     * 刷新，可运行在非UI线程
     */
    @Override
    public void postInvalidateCompat() {
        postInvalidateOnAnimation();
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
     * @param fps FPS (0~60)
     */
    @Override
    public void setFPS(@IntRange(from = 0, to = 60) int fps) {
        if (fps < 0 || fps > 60)
            throw new IllegalArgumentException("fps range: 0 ~ 60.");
        this.fps = fps;
        if (fps > 0)
            this.mDelay = 1000 / fps;
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
     * 自动刷新，当 {@link #getFPS()} 为 1~60 才有效
     */
    @Override
    public void startAutoRefresh() {
        if (fps <= 0 || fps > 60 || mDelay <= 0)
            throw new IllegalArgumentException("fps must be 1 ~ 60.");
        mProxy.start();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDraw.onDrawFrame(canvas);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableTouchEvent || mTouchEventListener == this)
            return super.onTouchEvent(event);
        return mTouchEventListener.onTouchEvent(event);
    }
}