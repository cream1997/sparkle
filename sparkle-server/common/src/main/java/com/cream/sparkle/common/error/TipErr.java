package com.cream.sparkle.common.error;

public class TipErr extends RuntimeException {
    public TipErr(String message) {
        super(message);
    }

    public TipErr(String message, Throwable cause) {
        super(message, cause);
    }

    public TipErr(Throwable cause) {
        super(cause);
    }
}
