package me.eyeseeu.kiosk.category.exception;

import me.eyeseeu.kiosk.common.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "me.eyeseeu.kiosk.category")
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFoundException(
        CategoryNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(NotOwnedCategoryException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedCategoryException(
        NotOwnedCategoryException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(ex.getMessage()));
    }

}
