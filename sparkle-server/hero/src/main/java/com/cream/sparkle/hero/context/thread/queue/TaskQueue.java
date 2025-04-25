package com.cream.sparkle.hero.context.thread.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class TaskQueue {

    // 队列任务数量告警阈值
    private static final int AlarmThreshold = 100;
    private final LinkedBlockingQueue<FutureTask<?>> allTask = new LinkedBlockingQueue<>();
    private volatile boolean activeFlag = false;
    private final ReentrantLock activeLock = new ReentrantLock();

    public <T> Future<T> addTask(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        return addTask(futureTask);
    }

    public Future<Void> addTask(Runnable task) {
        return addTask(new FutureTask<>(task, null));
    }

    private <T> Future<T> addTask(FutureTask<T> futureTask) {
        allTask.add(futureTask);

        final int taskSize = allTask.size();
        if (taskSize >= AlarmThreshold) {
            // 队列堆积很多任务，一定是程序哪里出现了问题，导致生产的消息处理不过来
            log.error("队列堆积任务过多, 请检查程序; 数量:{}", taskSize);
        }

        activeLock.lock();
        try {
            if (!activeFlag) {
                activeFlag = true;
                @SuppressWarnings("all")
                ExecutorService executorService = threadPool();
                executorService.execute(this::processTask);
            }
        } finally {
            activeLock.unlock();
        }
        return futureTask;
    }

    private void processTask() {
        try {
            while (true) {
                FutureTask<?> task = allTask.poll();
                if (task == null) {
                    break; // 队列为空，结束处理
                }
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("任务执行异常", e);
                }
            }
        } finally {
            activeLock.lock();
            try {
                // 处理完成后移除活动标志
                activeFlag = false;
                // 检查是否有新任务加入
                if (!allTask.isEmpty()) {
                    activeFlag = true;
                    @SuppressWarnings("all")
                    ExecutorService executorService = threadPool();
                    executorService.execute(this::processTask);
                }
            } finally {
                activeLock.unlock();
            }
        }
    }

    public abstract ExecutorService threadPool();
}
