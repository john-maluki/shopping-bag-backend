package dev.johnmaluki.shoppingbagbackend.handler;

import dev.johnmaluki.shoppingbagbackend.error.ErrorMessage;
import dev.johnmaluki.shoppingbagbackend.exception.DirectoryCreationException;
import dev.johnmaluki.shoppingbagbackend.exception.FileReadableException;
import dev.johnmaluki.shoppingbagbackend.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
@ResponseStatus
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorMessage>  handleEntityNotFound(
            EntityNotFoundException ex) {
        log.error(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(FileUploadException.class)
    protected ResponseEntity<ErrorMessage>  handleFileUploadException(
            FileUploadException ex) {
        log.error(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(DirectoryCreationException.class)
    protected ResponseEntity<ErrorMessage>  handleDirectoryCreationException(
            DirectoryCreationException ex) {
        log.error(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(FileReadableException.class)
    protected ResponseEntity<ErrorMessage>  handleFileReadableException(
            FileReadableException ex) {
        log.error(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }
}
