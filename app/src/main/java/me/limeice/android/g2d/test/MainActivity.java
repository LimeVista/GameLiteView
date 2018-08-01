package me.limeice.android.g2d.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.limeice.android.g2d.GameLiteView;

public class MainActivity extends AppCompatActivity implements GameLiteView.OnDrawFrame {

    private GameLiteView mLiteView;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLiteView = findViewById(R.id.game_view);
        mLiteView.setFPS(60);
        mLiteView.setOnDraw(this);
        mPaint.setColor(0xFF4C5A79);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLiteView.startAutoRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLiteView.stopAutoRefresh();
    }

    int w, h;

    long time = System.currentTimeMillis();

    /**
     * 作画
     *
     * @param canvas 画布
     */
    @Override
    public void onDrawFrame(@NonNull Canvas canvas) {
        final View v = mLiteView.getHolderView();
        if (v.getWidth() <= w || v.getHeight() <= h) {
            w = 0;
            h = 0;
        }
        w += 3;
        h += 3;
        if (Build.VERSION.SDK_INT >= 23)
            canvas.drawColor(Color.WHITE);
        canvas.drawRect(0, 0, w, h, mPaint);
        long t = System.currentTimeMillis();
        Log.d("Delay:", "=>" + (t - time));
        time = t;
    }
}
