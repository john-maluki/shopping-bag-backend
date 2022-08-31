package dev.johnmaluki.shoppingbagbackend.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    //TODO - Check later: On exception occurrence, it should return customized error body.
    // However, it doesn't.

    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleConflict(HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.sendError(403, ex.getMessage());
    }
}
