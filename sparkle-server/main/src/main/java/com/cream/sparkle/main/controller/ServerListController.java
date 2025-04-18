package com.cream.sparkle.main.controller;

import com.cream.sparkle.main.object.vo.GameServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ServerListController {

    @GetMapping("/serverList")
    public List<GameServer> serverList() {
        List<GameServer> gameServers = new ArrayList<>();
        /*
        fixme 先静态写死，后续可能改为读取配置，或者动态注册
         */
        gameServers.add(new GameServer("研发服", "127.0.0.1", 8889, 8899));
        return gameServers;
    }
}
