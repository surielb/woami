package com.gaya.whoami.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Suri on 11/10/2014.
 */
public class TimerView extends TextView {

    public interface OnTimerCompleteListener {

        /**
         * Notification that the timer has completed.
         */
        void onTimerComplete(TimerView timer1);

    }

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int mBaseTime;
    private int mTimer;
    private boolean mRunning;
    private boolean mVisible;
    private boolean mStarted;
    private OnTimerCompleteListener mCompleteListener;


    public OnTimerCompleteListener getCompleteListener() {
        return mCompleteListener;
    }

    public void setCompleteListener(OnTimerCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    private static final int TICK_WHAT = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(--mTimer);
                dispatchTimerTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
            }
        }
    };

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(mTimer);
                dispatchTimerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    public TimerView start() {
        mStarted = true;
        updateRunning();
        return this;
    }

    public TimerView stop() {
        mStarted = false;
        updateRunning();
        return this;
    }

    public TimerView reset(int time) {
        stop();
        mHandler.removeMessages(TICK_WHAT);
        mBaseTime = mTimer = time;
        return start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private void dispatchTimerTick() {
        if (mTimer <= 0) {
            stop();
            if (mCompleteListener != null)
                mCompleteListener.onTimerComplete(this);
        }

    }

    private void updateText(int time) {
        setTextColor(time < mBaseTime / 3 ? Color.RED : Color.GREEN);
        setText(String.valueOf(time));
    }

}
