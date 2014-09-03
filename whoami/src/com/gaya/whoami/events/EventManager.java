package com.gaya.whoami.events;


import com.gaya.whoami.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Suriel
 * Date: 1/17/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager implements IEventManager {


    public EventManager() {

        Logger.d("EventManager: Creation");

    }




    private final Map<Class<?>, Map<IEvent, Integer>>
            events = new HashMap<Class<?>, Map<IEvent, Integer>>();


    <T extends IEvent> void addListener(Map<T, Integer> map, T listener, int recalls) {
        synchronized (map) {
            map.put(listener, recalls);
        }
    }

    public static <T> void runEvent(Map<T, Integer> events, Executor<T> executor) {

        try {
            ArrayList<T> remove = new ArrayList<T>();

            Set<Map.Entry<T, Integer>> items = new HashSet<Map.Entry<T, Integer>>(events.entrySet());

            for (Map.Entry<T, Integer> listener : items) {
                T key = listener.getKey();
                if (key == null)
                    continue;

                try {
                    executor.Execute(key);
                } catch (Exception e) {
                    Logger.e(e);
                }

                Integer value = listener.getValue();
                if (value != IEventManager.INFINITE) {
                    if (value <= 1) {
                        remove.add(listener.getKey());
                    } else {
                        events.put(key, value - 1);
                    }
                }
            }


            for (T listener : remove) {
                Logger.d("EventManager: Removing event for dispose %s", listener);
                events.remove(listener);
            }
        } catch (Exception ex) {
            Logger.e(ex);
        }
    }


    @Override
    public <T extends IEvent> void addEvent(Class<T> type, T listener, int recalls) {
        Logger.d("EventManager: Adding event %s for %s (%d recalls)", type, listener, recalls);
        Map<IEvent, Integer> map;

        synchronized (events) {
            map = events.get(type);
            if (map == null) {
                events.put(type, map = (AbstractMap<IEvent, Integer>) new WeakHashMap<T, Integer>());
            }
        }


        addListener(map, listener, recalls);

    }

    @Override
    public <T extends IEvent> void removeEvent(final Class<T> type, final T listener) {


        Map<? extends IEvent, Integer> map;
        Logger.d("EventManager: Removing event %s for %s", type, listener);

        synchronized (events) {
            map = events.get(type);
        }

        if (map != null)
            synchronized (map) {
                map.remove(listener);
            }


    }

    @Override
    public <T extends IEvent> void fireEvent(Class<T> type, Executor<T> executor) {
        Map<IEvent, Integer> map;

        synchronized (events) {
            map = events.get(type);
        }


        if (map != null) {
            runEvent((Map<T, Integer>) map, executor);
        }
    }
}