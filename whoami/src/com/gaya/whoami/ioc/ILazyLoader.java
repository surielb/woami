package com.gaya.whoami.ioc;

/**
 * Created by Suri on 11/9/2014.
 */
public interface ILazyLoader {

    public enum ReadyState {
        /**
         * the class is loading
         */
        Loading,
        /**
         * the class is ready for use
         */
        Ready,
        /**
         * the class faulted during load and should not be used
         */
        Error,
    }

    /**
     * gets the current ready state of the class
     *
     * @return the current state
     */
    ReadyState getReadyState();

    /**
     * registers a callback to be called when the class is ready
     *
     * @param callback the callback to call when the ready state changes
     *                 if the class has already been initialized the callback will be called immediately
     */
    void registerReadyCallback(Callback<Boolean> callback);

    /**
     * initializes the lazy object (not called directly)
     */
    void init();
}
