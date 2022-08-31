package dev.johnmaluki.shoppingbagbackend.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessMessage extends Message{
    public SuccessMessage(
            String successMessage) {
        super(HttpStatus.ACCEPTED, successMessage);
    }
}
