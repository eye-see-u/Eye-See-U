package me.eyeseeu.kiosk.product.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.service.CategoryService;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.service.OptionGroupService;
import me.eyeseeu.kiosk.product.dto.request.ProductCreateRequest;
import me.eyeseeu.kiosk.product.dto.response.ProductCreateResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductGetResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductUpdateResponse;
import me.eyeseeu.kiosk.product.entity.Product;
import me.eyeseeu.kiosk.product.entity.ProductOptionGroup;
import me.eyeseeu.kiosk.product.exception.NotOwnedProductException;
import me.eyeseeu.kiosk.product.exception.ProductNotFoundException;
import me.eyeseeu.kiosk.product.repository.ProductOptionGroupRepository;
import me.eyeseeu.kiosk.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final OptionGroupService optionGroupService;

    @Transactional
    public ProductCreateResponse createProduct(Long memberId, ProductCreateRequest request) {
        Member member = memberService.findMemberById(memberId);

        Category category = categoryService.findCategoryById(memberId, request.categoryId());
        List<OptionGroup> optionGroups = optionGroupService.findAllOptionGroupById(memberId,
            request.optionGroups());

        List<ProductOptionGroup> productOptionGroups = optionGroups.stream()
            .map(ProductOptionGroup::new)
            .toList();

        Product product = Product.builder()
            .member(member)
            .category(category)
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .state(request.state())
            .productOptionGroups(new ArrayList<>())
            .picture(request.picture())
            .build();

        productOptionGroups.forEach(product::addProductOptionGroup);

        Product savedProduct = productRepository.save(product);
        productOptionGroupRepository.saveAll(productOptionGroups);

        return new ProductCreateResponse(
            savedProduct.getId(),
            savedProduct.getCategory().getId(),
            savedProduct.getProductOptionGroups().stream()
                .map(productOptionGroup -> productOptionGroup.getOptionGroup().getId())
                .toList(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.getPrice(),
            savedProduct.getState(),
            savedProduct.getPicture()
        );
    }

    public List<ProductGetResponse> getProducts(Long memberId) {
        List<Product> products = productRepository.findAllByMemberId(memberId);
        return products.stream()
            .map(product -> new ProductGetResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getProductOptionGroups().stream()
                    .map(productOptionGroup -> productOptionGroup.getOptionGroup().getId())
                    .toList(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getState(),
                product.getPicture()
            )).toList();
    }

    @Transactional
    public ProductUpdateResponse updateProduct(Long memberId, Long productId,
        ProductUpdateResponse request) {

        Product product = findProductById(memberId, productId);

        Category category = categoryService.findCategoryById(memberId, request.categoryId());
        List<OptionGroup> optionGroups = optionGroupService.findAllOptionGroupById(memberId,
            request.optionGroups());

        // 기존 옵션 그룹 제거 및 재등록
        product.clearProductOptionGroups();
        for (OptionGroup optionGroup : optionGroups) {
            product.addProductOptionGroup(ProductOptionGroup.builder()
                .optionGroup(optionGroup)
                .build());
        }

        product.update(
            request.name(), request.description(), request.price(), request.state(), category,
            request.picture()
        );

        Product savedProduct = productRepository.save(product);
        productOptionGroupRepository.saveAll(product.getProductOptionGroups());

        return new ProductUpdateResponse(
            savedProduct.getId(),
            savedProduct.getCategory().getId(),
            savedProduct.getProductOptionGroups().stream()
                .map(productOptionGroup -> productOptionGroup.getOptionGroup().getId())
                .toList(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.getPrice(),
            savedProduct.getState(),
            savedProduct.getPicture()
        );
    }

    public void deleteProduct(Long memberId, Long productId) {
        Product product = findProductById(memberId, productId);

        productRepository.delete(product);
    }

    public Product findProductById(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        if (!product.getCategory().getMember().getId().equals(memberId)) {
            throw new NotOwnedProductException();
        }

        return product;
    }
}
