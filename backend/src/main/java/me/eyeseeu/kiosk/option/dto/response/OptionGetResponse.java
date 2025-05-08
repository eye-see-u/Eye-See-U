package me.eyeseeu.kiosk.option.dto.response;

public record OptionGetResponse(
    Long id,
    String name,
    int price,
    String picture) {

}
