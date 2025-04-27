package me.eyeseeu.kiosk.member.exception;

public class InvalidCredentialsException extends RuntimeException {

    private static final String MESSAGE = "이메일 또는 비밀번호가 올바르지 않습니다.";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }

}
