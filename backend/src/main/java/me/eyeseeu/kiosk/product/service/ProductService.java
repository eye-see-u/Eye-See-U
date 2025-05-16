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
import me.eyeseeu.kiosk.product.entity.Product;
import me.eyeseeu.kiosk.product.entity.ProductOptionGroup;
import me.eyeseeu.kiosk.product.repository.ProductOptionGroupRepository;
import me.eyeseeu.kiosk.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final OptionGroupService optionGroupService;

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

}
