package com.gaya.whoami;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 * @author gaya
 *         Date: 10/15/13
 *         Time: 1:37 PM
 */
public class SafeHandler {
    private static Handler bg_handler;
    private static Looper bg_looper;
    private final static Handler main_handler = new Handler(Looper.getMainLooper());
    static CountDownLatch latch = new CountDownLatch(1);
    private final static Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            bg_looper = Looper.myLooper();
            bg_handler = new Handler(Looper.myLooper());
            if(latch != null)
                latch.countDown();
            Looper.loop();
        }
    });

    public static Looper getBgLooper(){return bg_looper;}
    static {
        thread.setName("Background Handler");
        thread.start();
    }

    public static Looper getBGLooper(){
        if(bg_handler == null)
            try {
                latch.await();
            } catch (InterruptedException e) {
                Logger.e(e);
            }
        return bg_handler.getLooper();
    }

    public static void post(Runnable what)
    {
        main_handler.post(what);
    }

    public static void postDelayed(Runnable what,long delayMs) {
        main_handler.postDelayed(what, delayMs);
    }

    public static void removeCallbacks(Runnable what)
    {
        main_handler.removeCallbacks(what);
    }
    public static void bgPost(Runnable what) {
        if (bg_handler != null)
            bg_handler.post(what);
        else
            post(what);
    }

    public static void bgPostDelayed(Runnable what, long delayMs) {
        if(bg_handler != null)
            bg_handler.postDelayed(what, delayMs);
        else
            postDelayed(what,delayMs);
    }

    public static void bgRemoveCallback(Runnable what)
    {
        if (bg_handler != null)
            bg_handler.removeCallbacks(what);
        else
            removeCallbacks(what);
    }
}
