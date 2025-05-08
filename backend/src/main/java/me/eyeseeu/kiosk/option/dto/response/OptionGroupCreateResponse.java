package me.eyeseeu.kiosk.option.dto.response;

import java.util.List;

public record OptionGroupCreateResponse(
    Long id,
    String name,
    int minCount,
    int maxCount,
    List<OptionCreateResponse> options
) {

}
