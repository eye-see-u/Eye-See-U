package me.eyeseeu.kiosk.category.exception;

public class CategoryNotFoundException extends RuntimeException {

    private static final String MESSAGE = "잘못된 카테고리 번호입니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

}
