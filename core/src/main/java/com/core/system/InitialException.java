package com.core.system;

/**
 * Created by laizy on 2017/6/7.
 */
public class InitialException extends RuntimeException {
    private static final long serialVersionUID = 5774978215489570268L;

    public InitialException() {
    }

    public InitialException(String message) {
        super(message);
    }

    public InitialException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitialException(Throwable cause) {
        super(cause);
    }

    public InitialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
