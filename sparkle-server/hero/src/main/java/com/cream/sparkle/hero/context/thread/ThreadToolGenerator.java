package com.cream.sparkle.hero.context.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadToolGenerator {

    private static final int CUP_CORE = Runtime.getRuntime().availableProcessors();
    private static final String COMMON_THREAD_NAME = "Hero-Common-Thread";
    private static final String ScheduledThreadNamePrefix = "Hero-Scheduled-Thread-";
    private static final String TmpThreadNamePrefix = "Hero-Tmp-Thread-";

    private static final String UserThreadNamePrefix = "Hero-User-Thread-";
    private static final String MapThreadNamePrefix = "Hero-Map-Thread-";

    private static final AtomicInteger ScheduledThreadNum = new AtomicInteger(0);
    private static final AtomicInteger TmpThreadNum = new AtomicInteger(0);

    private static final AtomicInteger UserThreadNum = new AtomicInteger(0);
    private static final AtomicInteger MapThreadNum = new AtomicInteger(0);


    public static ExecutorService geneCommonSingleThread() {
        return Executors.newSingleThreadExecutor(r -> {
            log.info("创建通用线程: {}", COMMON_THREAD_NAME);
            return new Thread(r, COMMON_THREAD_NAME);
        });
    }

    /**
     * todo数量待定
     */
    public static ScheduledExecutorService geneScheduledThreadPool() {
        return Executors.newScheduledThreadPool(4, r -> {
            String name = ScheduledThreadNamePrefix + ScheduledThreadNum.getAndIncrement();
            log.info("创建定时任务线程:{}", name);
            return new Thread(r, name);
        });
    }

    public static ExecutorService geneTmpThreadPool() {
        return Executors.newCachedThreadPool(r -> {
            String name = TmpThreadNamePrefix + TmpThreadNum.getAndIncrement();
            log.info("创建临时线程: {}", name);
            return new Thread(r, name);
        });
    }

    /**
     * todo 数量待定
     */
    public static ExecutorService geneUserThreadTool() {
        return Executors.newFixedThreadPool(CUP_CORE * 2, r -> {
            String name = UserThreadNamePrefix + UserThreadNum.getAndIncrement();
            log.info("创建游戏用户线程: {}", name);
            return new Thread(r, name);
        });
    }

    /**
     * todo 数量待定
     */
    public static ExecutorService geneMapThreadTool() {
        return Executors.newFixedThreadPool(CUP_CORE * 2, r -> {
            String name = MapThreadNamePrefix + MapThreadNum.getAndIncrement();
            log.info("创建地图线程: {}", name);
            return new Thread(r, name);
        });
    }
}
