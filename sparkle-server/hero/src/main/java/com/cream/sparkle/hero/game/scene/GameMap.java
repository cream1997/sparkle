package com.cream.sparkle.hero.game.scene;

import com.cream.sparkle.common.utils.IdUtil;
import com.cream.sparkle.hero.configuration.MapCfg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameMap {
    public final long id;
    public final Meta meta;

    public GameMap(MapCfg mapCfg) {
        this.id = IdUtil.generateUniqueId();
        this.meta = new Meta(mapCfg.getId(), mapCfg.getName(),
                mapCfg.getWidth(), mapCfg.getHeight());
    }


    public record Meta(int cfgId, String name, int width, int height) {
    }
}
