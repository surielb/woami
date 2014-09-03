package com.gaya.whoami.threading;

/**
 * Implementation of a java Manual reset event
 * @author suriel
 *         Date: 2/9/14
 *         Time: 11:46 PM
 */
public class ManualResetEvent {
    private final Object monitor = new Object();
    private volatile boolean open = false;

    public ManualResetEvent(boolean open) {
        this.open = open;
    }

    public void waitOne() throws InterruptedException {
        synchronized (monitor) {
            while (open == false) {
                monitor.wait();
            }
        }
    }

    public boolean waitOne(long milliseconds) throws InterruptedException {
        synchronized (monitor) {
            if (open)
                return true;
            monitor.wait(milliseconds);
            return open;
        }
    }

    public void set() {//open start
        synchronized (monitor) {
            open = true;
            monitor.notifyAll();
        }
    }

    public void reset() {//close stop
        open = false;
    }
}
