package com.cream.sparkle.common.error;

/**
 * 抛出后需要处理的通用异常
 * 之所以要定义这个类，是想缩短一下异常名字的长度
 */
public class Err extends Exception {

    public Err(String message) {
        super(message);
    }

    public Err(String message, Throwable cause) {
        super(message, cause);
    }

    public Err(Throwable cause) {
        super(cause);
    }
}