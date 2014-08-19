package com.gaya.whoami;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.gaya.whoami.questions.AssetsQuestionManager;
import com.gaya.whoami.questions.QuestionManager;

import java.lang.reflect.Field;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class Globals {
    private static Context appContext;
    private static QuestionManager questionManager;

    public synchronized static void init(Context context)
    {
        if(appContext != null)return;
        appContext = context.getApplicationContext();
        ImageLoaderHelpers.init(context);
    }
    public synchronized static QuestionManager getQuestionManager(){
        if(questionManager == null)
        {
            questionManager = new AssetsQuestionManager(appContext);
        }
        return questionManager;
    }
    private final static Field childFragmentManager ;
    static {
        Field mChildFragmentManager = null;
        try {
            mChildFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            mChildFragmentManager.setAccessible(true);


        } catch (NoSuchFieldException e) {

        }
        childFragmentManager= mChildFragmentManager;
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
