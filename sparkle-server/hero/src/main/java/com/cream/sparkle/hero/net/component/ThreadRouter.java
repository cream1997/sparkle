package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.queue.LoginThreadTaskQueue;
import com.cream.sparkle.hero.context.thread.queue.RoleThreadTaskQueue;
import com.cream.sparkle.hero.context.thread.queue.TaskQueue;
import com.cream.sparkle.hero.game.scene.GameMap;
import com.cream.sparkle.hero.manager.MapManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 消息处理的线程路由器
 */
@Slf4j
@Component
public class ThreadRouter {

    private static final ConcurrentHashMap<Long, LoginThreadTaskQueue> uid2LoginThreadTaskQueue = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, RoleThreadTaskQueue> rid2RoleThreadTaskQueue = new ConcurrentHashMap<>();

    private static MapManager mapManager;

    public ThreadRouter(MapManager mapManager) {
        ThreadRouter.mapManager = mapManager;
        ExecutorsUtil.runFixedDelay(() -> {
            cleanIdleQueue(rid2RoleThreadTaskQueue);
            cleanIdleQueue(uid2LoginThreadTaskQueue);
            // todo 先设置一个比较短的时间，观察是否有bug;后续可以改为比较长的时间
        }, 5, 5, TimeUnit.MINUTES);
    }

    private void cleanIdleQueue(ConcurrentHashMap<Long, ? extends TaskQueue> id2TaskQueue) {
        for (Long idleQueueId : getIdleQueue(id2TaskQueue)) {
            /*
             * 一定要使用compute来删除,这样能精准的加上键锁,回调函数也是键级别的原子操作;
             * 不要使用基于视图的删除比如entrySet.removeIf,粗略测试发现它不会基于键阻塞其它compute函数
             */
            id2TaskQueue.compute(idleQueueId, (k, v) -> {
                if (v != null && v.isIdle()) {
                    return null;
                }
                return v;
            });
        }
    }


    private List<Long> getIdleQueue(ConcurrentHashMap<Long, ? extends TaskQueue> id2Queue) {
        return id2Queue.entrySet().stream()
                .filter(idAndQueue -> idAndQueue.getValue().isIdle())
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * 路由至玩家线程
     */
    public static Future<Void> routing2Role(long rid, Runnable task) {
        FutureTask<Void> voidFutureTask = new FutureTask<>(task, null);
        rid2RoleThreadTaskQueue.compute(rid, (k, v) -> {
            if (v == null) {
                v = new RoleThreadTaskQueue();
            }
            v.addTask(voidFutureTask);
            return v;
        });
        return voidFutureTask;
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

    public static Future<Void> routing2Login(long uid, Runnable task) {
        FutureTask<Void> voidFutureTask = new FutureTask<>(task, null);
        uid2LoginThreadTaskQueue.compute(uid, (k, v) -> {
            if (v == null) {
                v = new LoginThreadTaskQueue();
            }
            v.addTask(voidFutureTask);
            return v;
        });
        return voidFutureTask;
    }
}
