package io.blueharvest.labs.axon.common.exception;

public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String msg) {
        super(msg);
    }
}
