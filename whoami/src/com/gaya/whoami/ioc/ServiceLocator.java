package com.gaya.whoami.ioc;

/**
 * @author Suriel
 *         Date: 1/22/12
 *         Time: 3:32 PM
 */
public class ServiceLocator {
    static IServiceLocator current_locator;

    public static void setCurrent(IServiceLocator locator) {
        current_locator = locator;
    }


    public static IServiceLocator getCurrent() {
        return current_locator;
    }


    public static <T> T getService(Class<T> classType, final Object... args) {
        return getCurrent().getService(classType, args);
    }

    public static <T> T registerService(Class<T> classType, T service) {
        return getCurrent().registerService(classType, service);
    }

    public static <T> void registerService(Class<T> classType, Class<? extends T> service) {
        getCurrent().registerService(classType, service);
    }

    public static <T> void unregisterService(Class<T> classType, T instance) {
        getCurrent().unregisterService(classType, instance);

    }

    public static <T> void registerServiceFactory(Class<T> classType, IServiceLocator.IFactory<? extends T> factory, boolean isSingleton)
    {
        getCurrent().registerServiceFactory(classType,factory,isSingleton);
    }
}
