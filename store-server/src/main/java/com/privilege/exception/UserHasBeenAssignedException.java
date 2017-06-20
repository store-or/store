package com.privilege.exception;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-16
 * Time: 下午5:44
 * To change this template use File | Settings | File Templates.
 */
public class UserHasBeenAssignedException extends RuntimeException {
    private static final long serialVersionUID = 5283436312776078302L;

    public UserHasBeenAssignedException() {
        super();
    }

    public UserHasBeenAssignedException(String message) {
        super(message);
    }

    public UserHasBeenAssignedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasBeenAssignedException(Throwable cause) {
        super(cause);
    }
}
