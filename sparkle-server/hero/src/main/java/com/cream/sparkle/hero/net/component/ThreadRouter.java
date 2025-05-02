package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.context.ExecutorsUtil;
import com.cream.sparkle.hero.context.thread.queue.TaskQueue;
import com.cream.sparkle.hero.context.thread.queue.UserThreadTaskQueue;
import com.cream.sparkle.hero.game.scene.GameMap;
import com.cream.sparkle.hero.manager.MapManager;
import com.cream.sparkle.hero.utils.Schedules;
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

    private static final ConcurrentHashMap<Long, UserThreadTaskQueue> uid2ThreadTaskQueue = new ConcurrentHashMap<>();

    private static MapManager mapManager;

    public ThreadRouter(MapManager mapManager) {
        ThreadRouter.mapManager = mapManager;
        // todo 先设置一个比较短的时间，观察是否有bug;后续可以改为比较长的时间
        Schedules.runFixedDelay(this::cleanIdleQueue, 5, 5, TimeUnit.MINUTES);
    }

    private void cleanIdleQueue() {
        List<Long> allIdleQueue = ((ConcurrentHashMap<Long, ? extends TaskQueue>) ThreadRouter.uid2ThreadTaskQueue).entrySet().stream()
                .filter(idAndQueue -> idAndQueue.getValue().isIdle())
                .map(Map.Entry::getKey)
                .toList();
        for (Long idleQueueId : allIdleQueue) {
            /*
             * 一定要使用compute来删除,这样能精准的加上键锁,回调函数也是键级别的原子操作;
             * 不要使用基于视图的删除比如entrySet.removeIf,粗略测试发现它不会基于键阻塞其它compute函数
             */
            ((ConcurrentHashMap<Long, ? extends TaskQueue>) ThreadRouter.uid2ThreadTaskQueue).compute(idleQueueId, (k, v) -> {
                if (v != null && v.isIdle()) {
                    return null;
                }
                return v;
            });
        }
    }

    /**
     * 路由至玩家线程
     */
    public static Future<Void> routing2User(long uid, Runnable task) {
        FutureTask<Void> voidFutureTask = new FutureTask<>(task, null);
        uid2ThreadTaskQueue.compute(uid, (k, v) -> {
            if (v == null) {
                v = new UserThreadTaskQueue();
            }
            v.addTask(voidFutureTask);
            return v;
        });
        return voidFutureTask;
    }

    /**
     * 路由到地图线程
     */
    public static Future<Void> routing2MapByUid(long uid, Runnable task) {
        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        routing2User(uid, () -> {
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
}
