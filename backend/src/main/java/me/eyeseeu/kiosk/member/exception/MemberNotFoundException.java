package me.eyeseeu.kiosk.member.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
