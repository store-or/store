package com.store.exception;

/**
 */
public class StoreException extends Exception {
    private static final long serialVersionUID = 37144652630865035L;
    private int errorCode;
    private Throwable cause ;

    public StoreException(String message) {
        super(message);
    }

    public StoreException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    public StoreException(int errorCode, String message, Throwable cause) {
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
