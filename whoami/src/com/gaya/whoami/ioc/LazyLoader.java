package com.gaya.whoami.ioc;

import com.gaya.whoami.Logger;

import java.util.HashSet;

public abstract class LazyLoader implements ILazyLoader {
    ReadyState mReadyState = ReadyState.Loading;
    Throwable loadException = null;

    protected boolean requiresThread() {
        return true;
    }

    public void init() {
        if (requiresThread()) {
            Thread loadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lazyLoad();
                }
            });
            loadThread.setPriority(Thread.NORM_PRIORITY);
            loadThread.start();
        } else
            lazyLoad();
    }

    final HashSet<Callback<Boolean>> callbacks = new HashSet<Callback<Boolean>>();
    private final Object locker = new Object();

    protected void ensureValidState() throws InvalidStateException {

        switch (getReadyState()) {
            case Ready:
                return;
            case Error:
                throw new InvalidStateException("Object is in exception state", loadException);
            case Loading:
                throw new InvalidStateException("Object is loading");
        }
    }

    protected abstract void lazyLoad();

    protected void setLoadComplete(Throwable e) {
        synchronized (locker) {
            if (mReadyState == ReadyState.Ready)
                return;
            loadException = e;
            mReadyState = e == null ? ReadyState.Ready : ReadyState.Error;
        }


        if (e != null) {
            Logger.w(e);
        }

        for (Callback<Boolean> callback : callbacks) {
            try {
                if (e != null) {
                    callback.callback(false,e);
                } else
                    callback.callback(true,null);
            } catch (Exception ex) {
                Logger.e(e);
            }
        }

        //Note: don't base the check on mReadyState because it can be modified by other threads. Using the per-invocation "e" param instead
        if (e == null) {
            callbacks.clear();
        }
    }

    @Override
    public ReadyState getReadyState() {
        return mReadyState;
    }

    @Override
    public void registerReadyCallback(Callback<Boolean> callback) {
        synchronized (locker) {
            if (getReadyState() == ReadyState.Loading) {
                callbacks.add(callback);
                return;
            }
        }

        if (loadException != null) {

            callback.callback(false,loadException);
        } else callback.callback(true,null);
    }


    /**
     * Thrown if the current state of a {@link ILazyLoader} is invalid for the request
     */
    public static class InvalidStateException extends RuntimeException {
        public InvalidStateException() {
        }

        public InvalidStateException(String detailMessage) {
            super(detailMessage);
        }

        public InvalidStateException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public InvalidStateException(Throwable throwable) {
            super(throwable);
        }
    }
}