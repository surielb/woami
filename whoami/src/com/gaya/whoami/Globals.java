package com.gaya.whoami;

import android.content.*;
import android.support.v4.app.*;
import com.gaya.whoami.events.*;
import com.gaya.whoami.players.*;
import com.gaya.whoami.questions.*;

import java.lang.reflect.*;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class Globals {
    private static Context appContext;
    private static QuestionManager questionManager;
    private static IEventManager eventManager;
    private static PlayerManager playerManager;

    public synchronized static void init(Context context) {
        if (appContext != null) return;
        appContext = context.getApplicationContext();
        ImageLoaderHelpers.init(context);
    }

    public synchronized static QuestionManager getQuestionManager() {
        if (questionManager == null) {
            questionManager = new AssetsQuestionManager(appContext);
        }
        return questionManager;
    }

    public static synchronized IEventManager eventManager() {
        if (eventManager == null)
            eventManager = new EventManager();
        return eventManager;
    }

    public static synchronized PlayerManager getPlayerManager() {
        if (playerManager == null)
            try {
                playerManager = new DummyPlayerManager(appContext);
            } catch (Exception e) {
                Logger.e(e);
            }
        return playerManager;
    }

    private final static Field childFragmentManager;

    static {
        Field mChildFragmentManager = null;
        try {
            mChildFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            mChildFragmentManager.setAccessible(true);


        } catch (NoSuchFieldException e) {

        }
        childFragmentManager = mChildFragmentManager;
    }

    public static void detachFragment(Fragment fragment) {
        if (childFragmentManager != null) {
            try {
                childFragmentManager.set(fragment, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }
}
