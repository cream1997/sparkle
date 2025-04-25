package com.cream.sparkle.hero.context.thread.queue;


import com.cream.sparkle.hero.context.ExecutorsUtil;

import java.util.concurrent.ExecutorService;

public class MapThreadTaskQueue extends TaskQueue {
    @Override
    public ExecutorService threadPool() {
        return ExecutorsUtil.MapThreadPool;
    }
}
