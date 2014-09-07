package com.gaya.whoami.events;


import com.gaya.whoami.*;

import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author gaya
 *         Date: 5/28/12
 *         Time: 1:26 PM
 */
public class EventHelper<K, V> {
    private final Map<K, List<WeakReference<V>>>
            changeEvents = new HashMap<K, List<WeakReference<V>>>();

    public void addListener(K key, V listener) {
        List<WeakReference<V>> callbacks;
        synchronized (changeEvents) {
            callbacks = changeEvents.get(key);
        }
        if (callbacks == null)
            changeEvents.put(key, callbacks = new CopyOnWriteArrayList<WeakReference<V>>());
        synchronized (callbacks) {
            callbacks.add(new WeakReference<V>(listener));
        }

    }

    public void removeListener(K key, V listener) {
        List<WeakReference<V>> callbacks;
        synchronized (changeEvents) {
            callbacks = changeEvents.get(key);
        }
        if (callbacks != null) {
            for (WeakReference<V> wr : callbacks) {
                if (wr.get() == listener) {
                    synchronized (callbacks) {
                        callbacks.remove(wr);
                    }
                    if (callbacks.size() == 0) {
                        synchronized (changeEvents) {
                            changeEvents.remove(key);
                        }
                    }

                    return;
                }
            }

        }
    }

    public void fireEvent(K key, IEventManager.Executor<V> executor) {
        List<WeakReference<V>> callbacks;
        synchronized (changeEvents) {
            callbacks = changeEvents.get(key);
        }
        if (callbacks != null) {
            //int size =callbacks.size();
            for (WeakReference<V> wr : callbacks) {
                //WeakReference<V> wr = callbacks.get(i);
                V run = wr.get();
                if (run == null) {
                    callbacks.remove(wr);
                } else {
                    try {
                        executor.Execute(run);
                    } catch (Exception ex) {
                        Logger.e(ex);
                    }
                }
            }
        }

    }

    public void clear() {
        changeEvents.clear();
    }
}
