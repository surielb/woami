package com.gaya.whoami.ioc;

/**
 * Created by Suri on 11/9/2014.
 */
public interface Callback<T> {
    void callback(T result,Throwable error);
}
