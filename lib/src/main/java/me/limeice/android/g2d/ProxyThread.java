package me.limeice.android.g2d;

final class ProxyThread implements Runnable {

    private final GameLiteView mLite;
    private final Object NOTIFY = new Object();

    private boolean isStart = false;
    private Thread mThread;

    ProxyThread(GameLiteView gameLiteView) {
        mLite = gameLiteView;
    }

    synchronized void start() {
        if (mThread != null) {
            isStart = false;
            mThread.interrupt();
        }
        mThread = new Thread(this);
        mThread.setPriority(8);
        isStart = true;
        mThread.start();
    }

    synchronized void stop() {
        if (mThread == null)
            return;
        isStart = false;
        mThread.interrupt();
        mThread = null;
    }

    @Override
    public void run() {
        while (isStart) {
            mLite.postInvalidateCompat();
            final int d = mLite.getDelay();
            if (d <= 0) continue;
            synchronized (NOTIFY) {
                try {
                    NOTIFY.wait(d);
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }
}
