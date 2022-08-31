package dev.johnmaluki.shoppingbagbackend.handler;

import dev.johnmaluki.shoppingbagbackend.error.ErrorMessage;
import dev.johnmaluki.shoppingbagbackend.exception.ShoppingBagAlreadyTrashedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Slf4j
@ControllerAdvice
@ResponseStatus
public class ShoppingBagAlreadyTrashedExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ShoppingBagAlreadyTrashedException.class)
    public ResponseEntity<ErrorMessage> shoppingBagAlreadyTrashedException(
            ShoppingBagAlreadyTrashedException exception) {
        log.error(exception.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.EXPECTATION_FAILED, exception.getMessage());
        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }
}
