package me.eyeseeu.kiosk.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import me.eyeseeu.kiosk.product.entity.ProductState;

public record ProductUpdateRequest(
    @NotNull Long categoryId,
    @NotNull List<Long> optionGroups,
    @NotBlank String name,
    @NotNull String description,
    @Min(0) int price,
    @NotNull ProductState state,
    String picture) {

}
