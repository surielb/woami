package com.gaya.whoami;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.gaya.whoami.network.BitmapLruCache;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class ImageLoaderHelpers {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    public synchronized static void init(Context context){
        if(mRequestQueue != null)return;

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        mImageLoader = new ImageLoader(mRequestQueue, imageCache);
    }

    public static ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
