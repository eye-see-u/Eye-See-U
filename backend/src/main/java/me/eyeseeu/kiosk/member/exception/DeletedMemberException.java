package me.eyeseeu.kiosk.member.exception;

public class DeletedMemberException extends RuntimeException {

    private static final String MESSAGE = "삭제된 회원입니다.";

    public DeletedMemberException() {
        super(MESSAGE);
    }

    public DeletedMemberException(String message) {
        super(message);
    }
}
