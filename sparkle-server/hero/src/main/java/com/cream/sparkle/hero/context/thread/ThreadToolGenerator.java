package com.cream.sparkle.hero.context.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadToolGenerator {

    private static final String COMMON_THREAD_NAME = "Hero-Common-Thread";
    private static final String ScheduledThread_NAME = "Hero-Scheduled-Thread";
    private static final String TmpThreadNamePrefix = "Hero-Tmp-Thread-";

    private static final String LoginThreadNamePrefix = "Hero-Login-Thread-";
    private static final String RoleThreadNamePrefix = "Hero-Role-Thread-";
    private static final String MapThreadNamePrefix = "Hero-Map-Thread-";

    private static final AtomicInteger TmpThreadNum = new AtomicInteger(0);

    private static final AtomicInteger LoginThreadNum = new AtomicInteger(0);
    private static final AtomicInteger RoleThreadNum = new AtomicInteger(0);
    private static final AtomicInteger MapThreadNum = new AtomicInteger(0);


    /**
     * todo 数量待定
     */
    public static ExecutorService geneLoginThreadPool() {
        return Executors.newFixedThreadPool(6, r -> {
            String name = LoginThreadNamePrefix + LoginThreadNum.getAndIncrement();
            log.info("创建登录线程: {}", name);
            return new Thread(r, name);
        });
    }

    public static ExecutorService geneCommonSingleThread() {
        return Executors.newSingleThreadExecutor(r -> {
            log.info("创建通用线程: {}", COMMON_THREAD_NAME);
            return new Thread(r, COMMON_THREAD_NAME);
        });
    }

    public static ScheduledExecutorService geneScheduledSingleThread() {
        return Executors.newSingleThreadScheduledExecutor(r -> {
            log.info("创建定时任务线程: {}", ScheduledThread_NAME);
            return new Thread(r, ScheduledThread_NAME);
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
    public static ExecutorService geneRoleThreadTool() {
        return Executors.newFixedThreadPool(16, r -> {
            String name = RoleThreadNamePrefix + RoleThreadNum.getAndIncrement();
            log.info("创建游戏玩家线程: {}", name);
            return new Thread(r, name);
        });
    }

    /**
     * todo 数量待定
     */
    public static ExecutorService geneMapThreadTool() {
        return Executors.newFixedThreadPool(16, r -> {
            String name = MapThreadNamePrefix + MapThreadNum.getAndIncrement();
            log.info("创建地图线程: {}", name);
            return new Thread(r, name);
        });
    }
}
