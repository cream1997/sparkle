package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.queue.RoleThreadTaskQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 消息处理的线程路由器
 */
@Slf4j
@Component
public class ThreadRouter {

    /**
     * 暂时不清理这个队列，因为一旦要加上清理逻辑，又需要加锁；可预见这个队列数量不会太大
     */
    private static final ConcurrentHashMap<Long, RoleThreadTaskQueue> rid2RoleThreadTaskQueue = new ConcurrentHashMap<>();

    /**
     * 路由至逻辑线程
     */
    public static Future<Void> routing2Role(long rid, Runnable task) {
        RoleThreadTaskQueue roleThreadTaskQueue = rid2RoleThreadTaskQueue.computeIfAbsent(rid, k -> new RoleThreadTaskQueue());
        return roleThreadTaskQueue.addTask(task);
    }

    /**
     * 路由到地图线程
     */
    public static Future<Void> routing2Map(long rid, Runnable task) {
        FutureTask<Void> voidFutureTask = new FutureTask<>(() -> {
        }, null);
        voidFutureTask.run();
        return voidFutureTask;
    }

    public static Future<Void> routing2Common(Runnable runnable) {
        return ExecutorsUtil.CommonSingleThread.submit(runnable, null);
    }

    public static Future<Void> routing2Login(Runnable runnable) {
        return ExecutorsUtil.LoginSingleThread.submit(runnable, null);
    }
}
