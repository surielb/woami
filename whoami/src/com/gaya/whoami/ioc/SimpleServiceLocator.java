package com.gaya.whoami.ioc;

import com.gaya.whoami.Logger;

import java.util.HashMap;

/**
 * A service locator that handles service creations
 */
public class SimpleServiceLocator implements IServiceLocator {

    private static volatile boolean initialized = false;

    public static synchronized void init(Runnable callback) {
        Logger.d("SimpleServiceLocator init:  initialized is: " + initialized);
        if (initialized) return;

        initialized = true;
        ServiceLocator.setCurrent(new SimpleServiceLocator());
        if(callback != null)
            callback.run();
       /* ServiceLocator.registerService(IContextProvider.class, new IContextProvider() {
            @Override
            public Context getContext() {
                return context;
            }
        });*/

    }

    private final static HashMap<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();


    static class factory {
        public boolean isSingleton;
        public IFactory<?> factory;

        factory(boolean singleton, IFactory<?> factory) {
            isSingleton = singleton;
            this.factory = factory;
        }
    }

    private final static HashMap<Class<?>, factory> factories = new HashMap<Class<?>, factory>();


    private static final Object locker = new Object();

    private SimpleServiceLocator() {
    }

    static <T> IFactory<T> getSimpleConstructor(final Class<T> classType) {
        return new IFactory<T>() {
            @Override
            public T createObject(Object... args) {
                try {
                    return (T) classType.newInstance();
                } catch (IllegalAccessException e) {
                    Logger.e(e);
                } catch (InstantiationException e) {
                    Logger.e(e);
                }
                return null;
            }
        };
    }

    public <T> T getService(Class<T> classType, final Object... args) {
        T obj;
        synchronized (classType) {
            //first check if we have a stored version
            if (singletons.containsKey(classType))
                return (T) singletons.get(classType);

            //check if we have a builder
            factory factory = factories.get(classType);

            if (factory == null) {
                Logger.i("Could not find service for class " + classType);
                return null;
            }

            obj = (T) factory.factory.createObject(args);
            if (factory.isSingleton)
                singletons.put(classType, obj);
        }

        if (obj instanceof ILazyLoader) {
            ((ILazyLoader) obj).init();
        }

        if (obj == null) {
            Logger.w("Could not find service for class " + classType);
        }

        return obj;
    }

    public <T> T registerService(Class<T> classType, T service) {
        synchronized (locker) {
            singletons.put(classType, service);
        }

        return service;
    }

    @Override
    public <T> void registerService(Class<T> classType, Class<? extends T> service) {

        registerServiceFactory(classType
                , getSimpleConstructor(service)
                , true);

        if(IPreload.class.isAssignableFrom(service))
        {
            Logger.d("Preloading %s",classType.getName());
            getService(classType);
        }
    }

    @Override
    public <T> void registerServiceFactory(Class<T> classType, IFactory<? extends T> factory, boolean isSingleton) {
        synchronized (locker) {
            factories.put(classType, new factory(isSingleton, factory));
        }
    }

    @Override
    public <T> void unregisterService(Class<T> classType, T instance) {
        synchronized (locker) {
            if (singletons.get(classType) == instance)
                singletons.remove(classType);
        }
    }

}
