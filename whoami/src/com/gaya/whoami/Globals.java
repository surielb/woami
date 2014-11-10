package com.gaya.whoami;

import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class Globals {

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


    public static CharSequence maskText(String text) {
        if (text == null || text.length() == 0)
            return text;
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c))
                sb.append(c);
            else
                sb.append('?');
        }
        return sb;
    }
}
