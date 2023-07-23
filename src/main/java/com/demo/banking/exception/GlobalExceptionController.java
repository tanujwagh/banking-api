package com.demo.banking.exception;

import com.demo.banking.dto.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = {ApiException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleReservationNotFoundException(Exception exception, ServletWebRequest request) {
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, ServletWebRequest request) {
        BindingResult result = ex.getBindingResult();
        String message = result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException, ServletWebRequest request) {
        return buildErrorResponse(notFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleReservationException(TransferException transferException, ServletWebRequest request) {
        return buildErrorResponse(transferException, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      HttpStatus httpStatus,
                                                      ServletWebRequest request) {
        return buildErrorResponse(exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(String message,
                                                      HttpStatus httpStatus,
                                                      ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(httpStatus.value())
                .path(request.getRequest().getRequestURI())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(httpStatus).body(body);
    }

}
