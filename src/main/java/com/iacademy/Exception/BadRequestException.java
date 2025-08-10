package com.iacademy.AcademySystem.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class BadRequestException extends RuntimeException {

    private final HttpStatus status;


    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status != null ? status : HttpStatus.BAD_REQUEST;
    }


    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String toString() {
        return "BadRequestException: " + getMessage() + ", Status=" + status;
    }
}
