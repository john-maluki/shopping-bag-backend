package dev.johnmaluki.shoppingbagbackend.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class Message {
    private final HttpStatus httpStatus;
    private final String message;

    public Message(
            HttpStatus httpStatus,
            String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
