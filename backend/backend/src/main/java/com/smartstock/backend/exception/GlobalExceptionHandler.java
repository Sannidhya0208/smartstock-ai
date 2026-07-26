package com.smartstock.backend.exception;

import com.smartstock.backend.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                404,
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }



    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicate(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {

        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                409,
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CONFLICT
        );
    }



    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {

        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                400,
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {

        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
) {

    String message = ex.getBindingResult()
            .getFieldErrors()
            .get(0)
            .getDefaultMessage();


    ApiResponse response = new ApiResponse(
            LocalDateTime.now(),
            400,
            message,
            request.getRequestURI()
    );


    return new ResponseEntity<>(
            response,
            HttpStatus.BAD_REQUEST
    );
}

}