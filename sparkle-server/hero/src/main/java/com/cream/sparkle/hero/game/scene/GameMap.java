package com.cream.sparkle.hero.game.scene;

import com.cream.sparkle.common.utils.IdUtil;
import com.cream.sparkle.hero.configuration.MapCfg;
import com.cream.sparkle.hero.context.thread.queue.MapThreadTaskQueue;
import com.cream.sparkle.hero.utils.Schedules;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GameMap {
    public final long id;
    public final Meta meta;
    private final MapThreadTaskQueue taskQueue;

    private final Map<Long, Player> id2Player = new HashMap<>();

    public GameMap(MapCfg mapCfg) {
        this.taskQueue = new MapThreadTaskQueue();
        this.id = IdUtil.generateUniqueId();
        this.meta = new Meta(mapCfg.getId(), mapCfg.getName(),
                mapCfg.getWidth(), mapCfg.getHeight());
        Schedules.runFixedDelay(() -> taskQueue.addTask(this::heartPerSecond), 0, 1, TimeUnit.SECONDS);
    }


    public record Meta(int cfgId, String name, int width, int height) {
    }


    private void heartPerSecond() {
//        log.debug("地图：{},执行心跳", this.meta.name);
    }

    public void enterMap(Player player) {

    }


    public Future<Void> addTask(Runnable task) {
        return taskQueue.addTask(task);
    }

}
