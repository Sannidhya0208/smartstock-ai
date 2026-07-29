package com.smartstock.backend.exception;

import com.smartstock.backend.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartstock.backend.exception.AiServiceException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(
                        ResourceNotFoundException exception,
                        HttpServletRequest request) {

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                exception.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(
                                errorResponse,
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequest(
                        BadRequestException exception,
                        HttpServletRequest request) {

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                exception.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(
                                errorResponse,
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(
                        Exception exception,
                        HttpServletRequest request) {

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "An unexpected error occurred",
                                request.getRequestURI());

                return new ResponseEntity<>(
                                errorResponse,
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(AiServiceException.class)
        public ResponseEntity<ErrorResponse> handleAiServiceException(
                        AiServiceException exception,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(error);
        }
}