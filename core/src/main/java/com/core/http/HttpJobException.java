package com.core.http;

/**
 * laizy
 */
public class HttpJobException extends Exception {
    private static final long serialVersionUID = -4082077744824650815L;
    private int errorCode;

    public HttpJobException(String message) {
        super(message);
    }

    public HttpJobException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpJobException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}