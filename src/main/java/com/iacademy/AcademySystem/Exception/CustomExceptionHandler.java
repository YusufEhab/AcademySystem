package com.iacademy.AcademySystem.Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException ex, Locale locale) {
        String message = resolveMessage(ex.getMessage(), locale);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .status(ex.getStatus())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestHandler(BadRequestException ex, Locale locale) {
        String message = resolveMessage(ex.getMessage(), locale);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentInvalidHandler(MethodArgumentNotValidException ex, Locale locale) {
        String validationMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(validationMessage)
                .status(HttpStatus.NOT_ACCEPTABLE)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, Locale locale) {
        ex.printStackTrace(); // لعرض الاستثناء في الـ Console أثناء التطوير
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Unexpected error occurred";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorMessage)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private String resolveMessage(String code, Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (Exception e) {
            System.err.println("Message code not found in messages.properties: " + code);
            return code;
        }

    }
}
