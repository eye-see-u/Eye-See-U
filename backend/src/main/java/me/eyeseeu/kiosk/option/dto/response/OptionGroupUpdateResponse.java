package me.eyeseeu.kiosk.option.dto.response;

import java.util.List;

public record OptionGroupUpdateResponse(
    Long id,
    String name,
    int minCount,
    int maxCount,
    List<OptionUpdateResponse> options
) {

}
