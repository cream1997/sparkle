package com.cream.sparkle.hero.utils;

import com.cream.sparkle.hero.context.ExecutorsUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Schedules {


    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return ExecutorsUtil.ScheduledThreadPool.schedule(task, delay, unit);
    }

    public static ScheduledFuture<?> runFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ExecutorsUtil.ScheduledThreadPool.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> runFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ExecutorsUtil.ScheduledThreadPool.scheduleWithFixedDelay(task, initialDelay, period, unit);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return ExecutorsUtil.TmpThreadPool.submit(task);
    }

    public static void execute(Runnable runnable) {
        ExecutorsUtil.TmpThreadPool.execute(runnable);
    }
}
