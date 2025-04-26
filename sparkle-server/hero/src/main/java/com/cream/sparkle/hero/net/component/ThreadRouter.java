package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.queue.LogicThreadTaskQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 消息处理的线程路由器
 */
@Slf4j
@Component
public class ThreadRouter {

    /**
     * 暂时不清理这个队列，因为一旦要加上清理逻辑，又需要加锁；可预见这个队列数量不会太大
     */
    private static final ConcurrentHashMap<Long, LogicThreadTaskQueue> rid2LogicThreadTaskQueue = new ConcurrentHashMap<>();

    /**
     * 路由至逻辑线程
     */
    public static void routing2Logic(long rid, Runnable task) {
        LogicThreadTaskQueue logicThreadTaskQueue = rid2LogicThreadTaskQueue.computeIfAbsent(rid, k -> new LogicThreadTaskQueue());
        logicThreadTaskQueue.addTask(task);
    }

    /**
     * 路由到地图线程
     */
    public static void routing2Map(long rid, Runnable task) {

    }

    public static void routing2Common(Runnable runnable) {
        ExecutorsUtil.CommonSingleThread.execute(runnable);
    }

    public static Future<Void> routing2Login(Runnable runnable) {
        return ExecutorsUtil.LoginSingleThread.submit(runnable, null);
    }
}
