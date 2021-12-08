package com.klutz.registry.core.exception;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class KlutzException extends RuntimeException{

    public KlutzException(String message) {
        super(message);
    }
    public KlutzException(Throwable cause) {
        super(cause);
    }
}
