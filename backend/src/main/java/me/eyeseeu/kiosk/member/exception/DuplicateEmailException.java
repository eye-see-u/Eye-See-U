package me.eyeseeu.kiosk.member.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

}
