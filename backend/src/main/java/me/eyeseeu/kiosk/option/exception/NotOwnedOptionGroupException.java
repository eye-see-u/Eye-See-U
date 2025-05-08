package me.eyeseeu.kiosk.option.exception;

public class NotOwnedOptionGroupException extends RuntimeException {

    private static final String MESSAGE = "해당 옵션그룹을 찾을 수 없습니다.";

    public NotOwnedOptionGroupException() {
        super(MESSAGE);
    }

}
