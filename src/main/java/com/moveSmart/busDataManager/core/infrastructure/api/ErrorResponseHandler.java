package com.moveSmart.busDataManager.core.infrastructure.api;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExistsException (
            EntityAlreadyExistsException ex, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex,
                HttpStatus.CONFLICT,
                "Entity already exists",
                null,
                new String[] {ex.getObjectType(), ex.getId()},
                request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException (
            EntityNotFoundException ex, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex,
                HttpStatus.NOT_FOUND,
                "Entity not found",
                null,
                new String[] {ex.getObjectType(), ex.getId()},
                request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
