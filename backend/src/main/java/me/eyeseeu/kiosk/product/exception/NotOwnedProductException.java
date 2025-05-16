package me.eyeseeu.kiosk.product.exception;

public class NotOwnedProductException extends RuntimeException {

    private static final String MESSAGE = "해당 상품을 찾을 수 없습니다.";

    public NotOwnedProductException() {
        super(MESSAGE);
    }

}
