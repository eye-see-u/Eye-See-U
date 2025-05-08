package me.eyeseeu.kiosk.option.dto.response;

public record OptionCreateResponse(
    Long id,
    String name,
    int price,
    String picture
) {

}
