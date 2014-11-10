package com.gaya.whoami.ioc;


/**
 * @author Suriel
 *         Date: 1/22/12
 *         Time: 3:31 PM
 */
public interface IServiceLocator {
    public interface IFactory<T> {
        T createObject(Object... args);
    }

    <T> T getService(Class<T> classType, final Object... args);

    <T> T registerService(Class<T> classType, T service);

    <T> void registerService(Class<T> classType, Class<? extends T> service);

    <T> void registerServiceFactory(Class<T> classType, IFactory<? extends T> factory, boolean isSingleton);

    <T> void unregisterService(Class<T> classType, T instance);
}