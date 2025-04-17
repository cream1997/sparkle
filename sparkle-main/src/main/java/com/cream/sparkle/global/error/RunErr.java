package com.cream.sparkle.global.error;

/**
 * 抛出后无需处理的通用异常
 */
public class RunErr extends RuntimeException {
    public RunErr(String message) {
        super(message);
    }

    public RunErr(String message, Throwable cause) {
        super(message, cause);
    }

    public RunErr(Err err) {
        super(err.getMessage());
    }

    public RunErr(Throwable cause) {
        super(cause);
    }
}

