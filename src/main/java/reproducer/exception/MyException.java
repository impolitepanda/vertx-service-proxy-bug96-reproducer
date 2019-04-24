package reproducer.exception;

import io.vertx.serviceproxy.ServiceException;

/**
 * Super class for all API exception. It is mainly used to be able to map internal errors to an HTTP status code.
 * Abstract as it absolutely needs to be specialized in order to
 */
public abstract class MyException extends ServiceException {

    public MyException(int failureCode, String message) {
        super(failureCode, message);
    }
}