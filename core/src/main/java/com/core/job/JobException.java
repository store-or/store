package com.core.job;

/**
 * Created by laizy on 2017/6/7.
 */
public class JobException extends Exception {
    private static final long serialVersionUID = -6768971998280361494L;

    public JobException() {
    }

    public JobException(String message) {
        super(message);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobException(Throwable cause) {
        super(cause);
    }
}
