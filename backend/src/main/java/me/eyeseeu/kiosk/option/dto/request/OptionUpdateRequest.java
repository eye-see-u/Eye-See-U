package me.eyeseeu.kiosk.option.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OptionUpdateRequest(
    @NotBlank String name,
    @Min(0) int price,
    String picture
) {

}
