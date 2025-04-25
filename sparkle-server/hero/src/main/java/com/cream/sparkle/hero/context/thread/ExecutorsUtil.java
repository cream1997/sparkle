package com.cream.sparkle.hero.context;

import com.cream.sparkle.hero.context.thread.ThreadToolGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 使用Executors创建的线程默认是用户线程，而不是守护线程
 * 用户线程：jvm会等待所有用户线程结束后才会退出
 * 守护线程：jvm不会等待守护线程结束（一旦所有用户线程终止，jvm会直接退出，即使守护线程仍在运行，也就是可能会被强行终止）
 * 这种区别在目前的spring应用中体现不出来；也就是说，它的区别在于用户线程如果不手动调用shutdown会阻塞main函数的结束
 * 而目前的程序本身启动后就是持续运行的；main的结束依赖于外部的信号比如kill -9或者kill -15
 * <p>
 * 即使创建的是用户线程，也不用在程序结束时，手动调用shutdown；因为如果是用kill -15，jvm会自动取shutdown,
 * 如果是kill -9直接暴力停止，手动调用的方法也执行不到
 */
@Slf4j
public class ExecutorsUtil {

    /**
     * 临时线程池(使用时创建,数量无上限)
     */
    private static final ExecutorService TmpThreadPool = ThreadToolGenerator.geneTmpThreadPool();
    /**
     * 定时任务线程(单线程)
     */
    private static final ScheduledExecutorService ScheduledSingleThread = ThreadToolGenerator.geneScheduledSingleThread();
    /**
     * 通用线程(单线程)
     */
    public static final ExecutorService CommonSingleThread = ThreadToolGenerator.geneCommonSingleThread();
    /**
     * 游戏逻辑线程池(固定数量)
     */
    public static final ExecutorService LogicThreadPool = ThreadToolGenerator.geneLogicThreadTool();
    /**
     * 地图线程池(固定数量)
     */
    public static final ExecutorService MapThreadPool = ThreadToolGenerator.geneMapThreadTool();

    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return ScheduledSingleThread.schedule(task, delay, unit);
    }

    public static ScheduledFuture<?> runFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ScheduledSingleThread.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> runFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ScheduledSingleThread.scheduleWithFixedDelay(task, initialDelay, period, unit);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return TmpThreadPool.submit(task);
    }

    public static void execute(Runnable runnable) {
        TmpThreadPool.execute(runnable);
    }
}
