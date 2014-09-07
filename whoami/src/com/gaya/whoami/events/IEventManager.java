package com.gaya.whoami.events;

/**
 * Created by IntelliJ IDEA.
 * User: gaya
 * Date: 1/17/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IEventManager {

    static final int INFINITE = -1;

    <T extends IEvent> void addEvent(Class<T> type, T listener, int recalls);

    <T extends IEvent> void removeEvent(Class<T> type, T listener);

    public interface Executor<T> {
        void Execute(T event);
    }

    <T extends IEvent> void fireEvent(Class<T> type, Executor<T> executor);


}