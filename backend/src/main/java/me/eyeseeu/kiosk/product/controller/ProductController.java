package me.eyeseeu.kiosk.product.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.product.dto.request.ProductCreateRequest;
import me.eyeseeu.kiosk.product.dto.request.ProductUpdateRequest;
import me.eyeseeu.kiosk.product.dto.response.ProductCreateResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductGetResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductUpdateResponse;
import me.eyeseeu.kiosk.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public List<ProductGetResponse> getProducts(@SessionAttribute("memberId") Long memberId) {
        return productService.getProducts(memberId);
    }

    @PutMapping("/{productId}")
    public ProductUpdateResponse updateProduct(@PathVariable Long productId,
        @RequestBody @Valid ProductUpdateRequest request,
        @SessionAttribute("memberId") Long memberId) {

        return productService.updateProduct(memberId, productId, request);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
        @PathVariable Long productId, @SessionAttribute("memberId") Long memberId) {

        productService.deleteProduct(memberId, productId);
    }
}
