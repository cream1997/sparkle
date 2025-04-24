package com.cream.sparkle.hero.context;

import com.cream.sparkle.hero.context.thread.ThreadToolGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 使用Executors创建的线程默认是用户线程，而不是守护线程
 * 用户线程：jvm会等待所有用户线程结束后才会退出
 * 守护线程：jvm不会等待守护线程结束（一旦所有用户线程终止，jvm会直接退出，即使守护线程仍在运行，也就是可能会被强行终止）
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
    private static final ExecutorService CommonSingleThread = ThreadToolGenerator.geneCommonSingleThread();
    /**
     * 游戏逻辑线程池(固定数量)
     */
    private static final ExecutorService LogicThreadPool = ThreadToolGenerator.geneLogicThreadTool();
    /**
     * 地图线程池(固定数量)
     */
    private static final ExecutorService MapThreadPool = ThreadToolGenerator.geneMapThreadTool();

    /**
     * 因为创建的都是用户线程，在应用退出后，需要调用shutdown(启动有序关闭：停止接受新任务，但会继续执行已提交的任务)
     */
    public static void shutdownAll() {
        TmpThreadPool.shutdown();
        ScheduledSingleThread.shutdown();
        CommonSingleThread.shutdown();
        LogicThreadPool.shutdown();
        MapThreadPool.shutdown();
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return ScheduledSingleThread.schedule(task, delay, unit);
    }

    public ScheduledFuture<?> runFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ScheduledSingleThread.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public ScheduledFuture<?> runFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return ScheduledSingleThread.scheduleWithFixedDelay(task, initialDelay, period, unit);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return TmpThreadPool.submit(task);
    }

    public void execute(Runnable runnable) {
        TmpThreadPool.execute(runnable);
    }
}
