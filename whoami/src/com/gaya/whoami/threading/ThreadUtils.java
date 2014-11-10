package com.gaya.whoami.threading;

import com.gaya.whoami.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Suri on 11/8/2014.
 */
public class ThreadUtils {
    final static ExecutorService executorService = Executors.newFixedThreadPool(255);

    public interface Factory<Tin, Tout> {
        Tout build(Tin item);
    }

    public static <Tout, Tin> List<Tout> executeAll(Collection<Tin> items, final Factory<Tin, Tout> factory) {

        final CountDownLatch latch = new CountDownLatch(items.size());
        final List<Tout> results = new ArrayList<Tout>(items.size());
        final List<Throwable> errors = new ArrayList<Throwable>(items.size());
        for (Tin itm : items) {
            final int pos = results.size();
            results.add(null);
            errors.add(null);
            final Tin item = itm;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        results.set(pos, factory.build(item));
                    } catch (Throwable ex) {
                        Logger.e(ex);
                        errors.set(pos, ex);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Logger.e(e);
        }
        return results;
    }


    public static <Tin> void executeAllAsync(Collection<Tin> items, final AsyncExecutor<Tin> executor) {

        final CountDownLatch latch = new CountDownLatch(items.size());

        final List<Throwable> errors = new ArrayList<Throwable>(items.size());
        for (Tin itm : items) {
            final int pos = errors.size();

            final Tin item = itm;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    final AtomicBoolean latched = new AtomicBoolean(false);
                    try {
                        executor.Execute(item, new Runnable() {
                            @Override
                            public void run() {
                                if (latched.compareAndSet(false, true))
                                    latch.countDown();
                            }
                        });
                    } catch (Throwable ex) {
                        Logger.e(ex);
                        errors.add(ex);
                        if (latched.compareAndSet(false, true))
                            latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Logger.e(e);
        }
        if(errors.size() >0)
            throw new AggregatedException(errors);

    }

    public interface Executor<T> {
        void Execute(T event);
    }


    public interface AsyncExecutor<T> {
        void Execute(T event, Runnable callback);
    }

    public static class AggregatedException extends RuntimeException {
        final Collection<Throwable> exceptions;

        public AggregatedException(Collection<Throwable> exceptions) {
            this.exceptions = exceptions;
        }

        public Collection<Throwable> getExceptions() {
            return exceptions;
        }
    }

    public static <T> void executeAll(final T item, AsyncExecutor<T>... executors) {
        executeAll(item, Arrays.asList(executors));
    }

    public static <T> void executeAll(final T item, Collection<AsyncExecutor<T>> executors) {
        final CountDownLatch latch = new CountDownLatch(executors.size());

        final List<Throwable> errors = new ArrayList<Throwable>(executors.size());

        for (AsyncExecutor<T> executor : executors) {
            final AsyncExecutor<T> exec = executor;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        exec.Execute(item, new Runnable() {
                            private boolean run;

                            @Override
                            public void run() {
                                if (run) return;
                                run = true;
                                latch.countDown();
                            }
                        });
                    } catch (Throwable ex) {
                        Logger.e(ex);
                        errors.add(ex);
                        latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Logger.e(e);
            errors.add(e);
        }
        if (errors.size() > 0)
            throw new AggregatedException(errors);
    }

    public static <T> void executeAll(final T item, Executor<T>... executors) {
        final CountDownLatch latch = new CountDownLatch(executors.length);

        final List<Throwable> errors = new ArrayList<Throwable>(executors.length);
        for (Executor<T> executor : executors) {
            final Executor<T> exec = executor;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        exec.Execute(item);
                    } catch (Throwable ex) {
                        Logger.e(ex);
                        errors.add(ex);

                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Logger.e(e);
            errors.add(e);
        }
        if (errors.size() > 0)
            throw new AggregatedException(errors);
    }

}