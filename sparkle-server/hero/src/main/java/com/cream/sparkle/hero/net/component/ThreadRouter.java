package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.queue.LoginThreadTaskQueue;
import com.cream.sparkle.hero.context.thread.queue.RoleThreadTaskQueue;
import com.cream.sparkle.hero.game.scene.GameMap;
import com.cream.sparkle.hero.manager.MapManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
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
     * 登录队列，暂不清理这个队列
     */
    private static final ConcurrentHashMap<Long, LoginThreadTaskQueue> uid2LoginThreadTaskQueue = new ConcurrentHashMap<>();
    /**
     * 暂时不清理这个队列，因为一旦要加上清理逻辑，又需要加锁；可预见这个队列数量不会太大
     */
    private static final ConcurrentHashMap<Long, RoleThreadTaskQueue> rid2RoleThreadTaskQueue = new ConcurrentHashMap<>();

    private static MapManager mapManager;

    public ThreadRouter(MapManager mapManager) {
        ThreadRouter.mapManager = mapManager;
    }

    /**
     * 路由至玩家线程
     */
    public static Future<Void> routing2Role(long rid, Runnable task) {
        RoleThreadTaskQueue roleThreadTaskQueue = rid2RoleThreadTaskQueue.computeIfAbsent(rid, k -> new RoleThreadTaskQueue());
        return roleThreadTaskQueue.addTask(task);
    }

    /**
     * 路由到地图线程
     */
    public static Future<Void> routing2MapByRid(long rid, Runnable task) {
        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        routing2Role(rid, () -> {
            // todo getMapId
            int mapId = 0;
            routing2MapByMapId(mapId, futureTask);
        });
        return futureTask;
    }

    /**
     * 路由到地图线程
     */
    public static Future<Void> routing2MapByMapId(long mapId, Runnable task) {
        GameMap map = mapManager.getMapById(mapId);
        if (map == null) {
            log.error("获取地图为空,mapId:{}", mapId);
            return CompletableFuture.completedFuture(null);
        }
        return map.addTask(task);
    }

    public static Future<Void> routing2Common(Runnable task) {
        return ExecutorsUtil.CommonSingleThread.submit(task, null);
    }

    public static Future<Void> routing2Login(long uid, Runnable runnable) {
        LoginThreadTaskQueue loginThreadTaskQueue = uid2LoginThreadTaskQueue.computeIfAbsent(uid, k -> new LoginThreadTaskQueue());
        return loginThreadTaskQueue.addTask(runnable);
    }
}
