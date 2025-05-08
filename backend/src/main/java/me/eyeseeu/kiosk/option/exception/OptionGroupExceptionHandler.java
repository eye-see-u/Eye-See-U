package me.eyeseeu.kiosk.option.exception;

import me.eyeseeu.kiosk.common.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "me.eyeseeu.kiosk.option")
public class OptionGroupExceptionHandler {

    @ExceptionHandler(OptionGroupNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOptionGroupNotFoundException(
        OptionGroupNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(NotOwnedOptionGroupException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedOptionGroupException(
        NotOwnedOptionGroupException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(ex.getMessage()));
    }
}
