package com.iacademy.AcademySystem.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;




@Getter
public class NotFoundException extends RuntimeException {

    private final HttpStatus status;


    public NotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status != null ? status : HttpStatus.NOT_FOUND;
    }


    public NotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    @Override
    public String toString() {
        return "NotFoundException: " + getMessage() + ", Status=" + status;
    }
}
