package com.cream.sparkle.hero.manager;

import com.cream.sparkle.hero.configuration.MapCfg;
import com.cream.sparkle.hero.game.role.Role;
import com.cream.sparkle.hero.game.scene.GameMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MapManager {

    private final ConcurrentHashMap<Long, GameMap> id2GameMap = new ConcurrentHashMap<>();
    @Getter
    private final GameMap mainCity;

    public MapManager() {
        mainCity = initAllCommonMap();
    }

    /**
     * 初始化所有常规地图
     */
    private GameMap initAllCommonMap() {
        MapCfg mapCfg = new MapCfg(1, "提瓦特大陆", 200, 200);
        GameMap gameMap = new GameMap(mapCfg);
        id2GameMap.put(gameMap.id, gameMap);
        log.info("常规地图加载完成...");
        return gameMap;
    }

    public void loginMap(Role role) {
        long mapId = role.basic.getMapId();
        GameMap targetMap;
        if (mapId == 0) {
            targetMap = mainCity;
        } else {
            targetMap = id2GameMap.get(mapId);
        }
        if (targetMap == null) {
            targetMap = mainCity;
        }

        targetMap.enterMap(null);
    }

    public GameMap getMapById(long mapId) {
        GameMap gameMap = id2GameMap.get(mapId);
        if (gameMap == null) {
            log.error("获取地图为空, mapId:{}", mapId);
        }
        return gameMap;
    }
}
