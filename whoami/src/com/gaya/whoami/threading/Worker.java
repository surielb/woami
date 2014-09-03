package com.gaya.whoami.threading;

import android.os.*;
import com.gaya.whoami.*;

/**
 * @author suriel
 *         Date: 1/14/14
 *         Time: 1:12 PM
 */
public class Worker {
    private Handler handler;
    final ManualResetEvent resetEvent;

    ManualResetEvent ready = new ManualResetEvent(false);

    private final Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    try {
                        resetEvent.waitOne();
                        msg.getCallback().run();
                    } catch (Exception e) {
                        Logger.e(e);
                    }
                }
            };
            ready.set();
            Looper.loop();
        }
    });

    public Worker(String name) {
        this(name, new ManualResetEvent(true));
    }

    public Worker(String name, ManualResetEvent resetEvent) {
        this.resetEvent = resetEvent;
        thread.setName(name);
        thread.start();
    }

    public void post(Runnable runnable) {
        ensureReady();
        handler.post(runnable);
    }

    private void ensureReady() {
        try {
            ready.waitOne();
        } catch (InterruptedException e) {
            Logger.e(e);
        }
    }

    public void postDelayed(Runnable runnable, long delay) {
        ensureReady();
        handler.postDelayed(runnable, delay);
    }

    public void removeCallbacks(Runnable runnable) {
        ensureReady();
        handler.removeCallbacks(runnable);
    }
}
