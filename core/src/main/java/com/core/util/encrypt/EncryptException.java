package com.core.util.encrypt;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-22
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class EncryptException extends Exception {
    private int errorCode;
    private Throwable cause ;

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    public EncryptException(int errorCode, String message, Throwable cause) {
        this(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
