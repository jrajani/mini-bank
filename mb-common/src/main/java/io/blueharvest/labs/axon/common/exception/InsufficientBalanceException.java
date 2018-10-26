package io.blueharvest.labs.axon.common.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super();
    }

    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
