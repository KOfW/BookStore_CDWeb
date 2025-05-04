/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nlu.cdweb.BookStore.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Luôn log exception trước khi xử lý
    private ResponseEntity<ResourceError> buildErrorResponse(HttpStatus status, String message, Exception ex) {
        log.error("Exception occurred: ", ex);
        var errorResponse = new ResourceError(status.value(), message, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResourceError> handleResourceNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ResourceError> handleAlreadyExistsException(AlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ResourceError> handleOptimisticLockException(OptimisticLockException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ResourceError> handleInsufficientInventoryException(InsufficientInventoryException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(InvalidBodyRequestException.class)
    public ResponseEntity<ResourceError> handleInvalidBodyRequestException(InvalidBodyRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    // Xử lý CustomServerException trước vì nó là exception cụ thể
    @ExceptionHandler(CustomServerException.class)
    public ResponseEntity<ResourceError> handleServerException(CustomServerException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }

    // Xử lý chung cho mọi exception còn lại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResourceError> handleGeneralException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(),
                ex
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResourceError> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResourceError> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResourceError> handleBadCredentialsException(BadCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password", ex);
    }
}