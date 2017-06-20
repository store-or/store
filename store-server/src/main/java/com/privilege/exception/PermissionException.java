package com.privilege.exception;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-16
 * Time: 下午5:44
 * To change this template use File | Settings | File Templates.
 */
public class PermissionException extends RuntimeException {

    private static final long serialVersionUID = -9127975861145346602L;

    public PermissionException() {
        super();
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }
}
