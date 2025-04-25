package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.task.LogicThreadTaskQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理的线程路由器
 */
@Component
public class ThreadRouter {

    private final ConcurrentHashMap<Long, LogicThreadTaskQueue> rid2LogicThreadTaskQueue = new ConcurrentHashMap<>();

    /**
     * 路由至逻辑线程
     */
    public void routing2Logic(long rid, Runnable task) {
        LogicThreadTaskQueue logicThreadTaskQueue = rid2LogicThreadTaskQueue.computeIfAbsent(rid, k -> new LogicThreadTaskQueue());
        logicThreadTaskQueue.addTask(task);
    }

    /**
     * 路由到地图线程
     */
    public void routing2Map(long rid, Runnable task) {

    }

    public void routing2Common(Runnable runnable) {
        ExecutorsUtil.CommonSingleThread.execute(runnable);
    }
}
