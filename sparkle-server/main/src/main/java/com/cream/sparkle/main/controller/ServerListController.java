package com.cream.sparkle.main.controller;

import com.cream.sparkle.main.object.vo.GameServerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ServerListController {

    @GetMapping("/serverList")
    public List<GameServerVO> serverList() {
        List<GameServerVO> gameServerVOS = new ArrayList<>();
        /*
        fixme 先静态写死，后续可能改为读取配置，或者动态注册
         */
        gameServerVOS.add(new GameServerVO("127.0.0.1", 8889, 0));
        return gameServerVOS;
    }
}
