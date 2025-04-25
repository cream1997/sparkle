package com.cream.sparkle.hero.context.thread.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class TaskQueue {

    private final LinkedBlockingQueue<FutureTask<?>> allTask = new LinkedBlockingQueue<>(100);
    private volatile boolean activeFlag = false;
    private final ReentrantLock lock = new ReentrantLock();

    public <T> Future<T> addTask(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        return addTask(futureTask);
    }

    public Future<Void> addTask(Runnable task) {
        return addTask(new FutureTask<>(task, null));
    }

    private <T> Future<T> addTask(FutureTask<T> futureTask) {
        try {
            allTask.add(futureTask);
        } catch (IllegalStateException e) {
            // 队列会出现满了的情况，一定是程序哪里出现了问题，导致生产的消息处理不过来
            log.error("队列已满, 请检查程序", e);
            return null;
        }
        lock.lock();
        try {
            if (!activeFlag) {
                activeFlag = true;
                @SuppressWarnings("all")
                ExecutorService executorService = threadPool();
                executorService.execute(this::processTask);
            }
        } finally {
            lock.unlock();
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
            lock.lock();
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
                lock.unlock();
            }
        }
    }


    public abstract ExecutorService threadPool();
}
