package me.eyeseeu.kiosk.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.product.dto.request.ProductCreateRequest;
import me.eyeseeu.kiosk.product.dto.response.ProductCreateResponse;
import me.eyeseeu.kiosk.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreateResponse createProduct(
        @Valid @RequestBody ProductCreateRequest request,
        @SessionAttribute("memberId") Long memberId) {

        return productService.createProduct(memberId, request);
    }
}
