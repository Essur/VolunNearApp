package com.volunnear.exception.handler;

import com.volunnear.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadUserCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentialsException(BadUserCredentialsException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<AppError> handleDataNotFoundException(DataNotFoundException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthErrorException.class)
    public ResponseEntity<AppError> handleAuthErrorException(AuthErrorException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AppError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadDataInRequestException.class)
    public ResponseEntity<AppError> handleBadDataInRequestException(BadDataInRequestException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<AppError> handleTokenRefreshException(TokenRefreshException e) {
        log.trace(e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
