package me.eyeseeu.kiosk.option.dto.response;

import java.util.List;

public record OptionGroupGetResponse(
    Long id,
    String name,
    int minCount,
    int maxCount,
    List<OptionGetResponse> options
) {

    public record OptionGetResponse(
        Long id,
        String name,
        int price,
        String picture
    ) {

    }

}
