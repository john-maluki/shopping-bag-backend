package dev.johnmaluki.shoppingbagbackend.util;

import org.springframework.http.HttpStatus;

public class FailureMessage extends Message{
    public FailureMessage(
            String failureMessage
            ) {
        super(HttpStatus.EXPECTATION_FAILED, failureMessage);
    }
}
