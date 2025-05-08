package me.eyeseeu.kiosk.option.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OptionGroupCreateRequest(
    @NotBlank String name,
    @Min(0) int minCount,
    @Min(0) int maxCount,
    @NotEmpty List<OptionCreateRequest> options
) {

}
