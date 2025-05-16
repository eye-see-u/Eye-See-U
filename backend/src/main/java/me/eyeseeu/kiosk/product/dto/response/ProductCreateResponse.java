package me.eyeseeu.kiosk.product.dto.response;

import java.util.List;
import me.eyeseeu.kiosk.product.entity.ProductState;

public record ProductCreateResponse(
    Long id,
    Long categoryId,
    List<Long> optionGroups,
    String name,
    String description,
    int price,
    ProductState state,
    String picture
) {

}
