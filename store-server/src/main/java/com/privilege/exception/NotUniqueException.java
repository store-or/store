package com.privilege.exception;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-16
 * Time: 下午5:44
 * To change this template use File | Settings | File Templates.
 */
public class NotUniqueException extends RuntimeException {

    private static final long serialVersionUID = 6490427615503867860L;

    public NotUniqueException() {
        super();
    }

    public NotUniqueException(String message) {
        super(message);
    }

    public NotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueException(Throwable cause) {
        super(cause);
    }
}
