package com.core.web.freemarker;

/**
 */
public class FreemarkerParseException extends Exception {
    private static final long serialVersionUID = -713139427853906645L;
    private int errorCode;
    private Throwable cause ;

    public FreemarkerParseException(String message) {
        super(message);
    }

    public FreemarkerParseException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public FreemarkerParseException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    public FreemarkerParseException(int errorCode, String message, Throwable cause) {
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
