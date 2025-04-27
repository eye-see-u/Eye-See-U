package me.eyeseeu.kiosk.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    String name
) {

}
