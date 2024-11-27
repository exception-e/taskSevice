package ru.test.taskservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.test.taskservice.exception.ErrorResponse;
import ru.test.taskservice.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;

import java.io.IOException;
import java.time.LocalDateTime;

@Hidden
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> springHandleNotFound(Exception ex, WebRequest request) throws IOException {
        ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError("Task " + ex.getMessage()+" not found");
        errors.setStatus(HttpStatus.NOT_FOUND.value());
       return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}