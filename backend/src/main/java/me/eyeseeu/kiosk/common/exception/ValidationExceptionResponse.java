package me.eyeseeu.kiosk.common.exception;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

public record ValidationExceptionResponse(
    List<ValidationError> errors
) {

    @Getter
    @Builder
    public static class ValidationError {

        private final String field;
        private final String message;
    }
}
