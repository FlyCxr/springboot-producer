package com.springboot.exception;

/**
 * 业务异常
 */
@SuppressWarnings("all")
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super();
    }

    public BusinessException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public BusinessException(String paramString) {
        super(paramString);
    }

    public BusinessException(Throwable paramThrowable) {
        super(paramThrowable);
    }

}
