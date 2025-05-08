package me.eyeseeu.kiosk.option.dto.response;

public record OptionUpdateResponse(
    Long id,
    String name,
    int price,
    String picture
) {

}
