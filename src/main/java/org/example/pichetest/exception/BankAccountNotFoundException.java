package org.example.pichetest.exception;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
