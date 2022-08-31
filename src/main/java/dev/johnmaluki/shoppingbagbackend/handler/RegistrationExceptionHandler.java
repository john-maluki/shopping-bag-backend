package dev.johnmaluki.shoppingbagbackend.handler;

import dev.johnmaluki.shoppingbagbackend.error.ErrorMessage;
import dev.johnmaluki.shoppingbagbackend.exception.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Slf4j
@ControllerAdvice
@ResponseBody
public class RegistrationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorMessage> userWithUsernameAlreadyExists(UserRegistrationException ex){
        log.error(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }
}
