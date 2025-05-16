package me.eyeseeu.kiosk.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.service.CategoryService;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.service.OptionGroupService;
import me.eyeseeu.kiosk.product.dto.request.ProductCreateRequest;
import me.eyeseeu.kiosk.product.dto.request.ProductUpdateRequest;
import me.eyeseeu.kiosk.product.dto.response.ProductCreateResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductGetResponse;
import me.eyeseeu.kiosk.product.dto.response.ProductUpdateResponse;
import me.eyeseeu.kiosk.product.entity.Product;
import me.eyeseeu.kiosk.product.entity.ProductOptionGroup;
import me.eyeseeu.kiosk.product.entity.ProductState;
import me.eyeseeu.kiosk.product.exception.NotOwnedProductException;
import me.eyeseeu.kiosk.product.exception.ProductNotFoundException;
import me.eyeseeu.kiosk.product.repository.ProductOptionGroupRepository;
import me.eyeseeu.kiosk.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOptionGroupRepository productOptionGroupRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private OptionGroupService optionGroupService;

    @Test
    @DisplayName("상품 생성 성공")
    void createProductSuccess() {
        // given
        Long memberId = 1L;
        Long categoryId = 1L;
        List<Long> optionGroupIds = List.of(1L, 2L);

        String productName = "불고기버거";
        String productDescription = "특제 불고기 소스로 입맛 돋우는 부드러운 버거";
        int price = 6000;
        ProductState productState = ProductState.AVAILABLE;
        String picture = "pic.jpg";

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Category category = Category.builder().member(member).build();
        ReflectionTestUtils.setField(category, "id", categoryId);

        OptionGroup optionGroup1 = OptionGroup.builder()
            .member(member)
            .name("옵션1")
            .minCount(1)
            .maxCount(1)
            .build();
        OptionGroup optionGroup2 = OptionGroup.builder()
            .member(member)
            .name("옵션2")
            .minCount(0)
            .maxCount(2)
            .build();
        ReflectionTestUtils.setField(optionGroup1, "id", optionGroupIds.getFirst());
        ReflectionTestUtils.setField(optionGroup2, "id", optionGroupIds.getLast());

        ProductCreateRequest request = new ProductCreateRequest(categoryId, optionGroupIds,
            productName, productDescription, price, productState, picture);

        given(memberService.findMemberById(memberId)).willReturn(member);
        given(categoryService.findCategoryById(memberId, categoryId)).willReturn(category);
        given(optionGroupService.findAllOptionGroupById(memberId, optionGroupIds)).willReturn(
            List.of(optionGroup1, optionGroup2));
        given(productRepository.save(any()))
            .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        ProductCreateResponse response = productService.createProduct(memberId, request);

        // then
        assertThat(response.categoryId()).isEqualTo(categoryId);
        assertThat(response.optionGroups()).isEqualTo(optionGroupIds);
        assertThat(response.name()).isEqualTo(productName);
        assertThat(response.description()).isEqualTo(productDescription);
        assertThat(response.price()).isEqualTo(price);
        assertThat(response.state()).isEqualTo(productState);
        assertThat(response.picture()).isEqualTo(picture);

        then(productOptionGroupRepository).should().saveAll(any());
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void getProductsSuccess() {
        // given
        Long memberId = 1L;
        Long categoryId = 1L;
        List<Long> optionGroupIds = List.of(1L, 2L);

        String productName1 = "불고기버거";
        String productName2 = "치즈버거";
        String productDescription1 = "특제 불고기 소스로 입맛 돋우는 부드러운 버거";
        String productDescription2 = "두 장의 육즙 가득한 패티와 고소한 치즈 두 장이 어우러진 풍성한 버거";
        int price1 = 6000;
        int price2 = 5000;
        ProductState productState1 = ProductState.AVAILABLE;
        ProductState productState2 = ProductState.AVAILABLE;
        String picture1 = "pic1.jpg";
        String picture2 = "pic2.jpg";

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Category category = Category.builder().member(member).build();
        ReflectionTestUtils.setField(category, "id", categoryId);

        OptionGroup optionGroup1 = OptionGroup.builder()
            .member(member)
            .name("옵션1")
            .minCount(1)
            .maxCount(1)
            .build();
        OptionGroup optionGroup2 = OptionGroup.builder()
            .member(member)
            .name("옵션2")
            .minCount(0)
            .maxCount(2)
            .build();
        ReflectionTestUtils.setField(optionGroup1, "id", optionGroupIds.getFirst());
        ReflectionTestUtils.setField(optionGroup2, "id", optionGroupIds.getLast());

        Product product1 = Product.builder()
            .member(member)
            .category(category)
            .productOptionGroups(new ArrayList<>())
            .name(productName1)
            .description(productDescription1)
            .price(price1)
            .state(productState1)
            .picture(picture1)
            .build();
        Product product2 = Product.builder()
            .member(member)
            .category(category)
            .productOptionGroups(new ArrayList<>())
            .name(productName2)
            .description(productDescription2)
            .price(price2)
            .state(productState2)
            .picture(picture2)
            .build();

        product1.addProductOptionGroup(ProductOptionGroup.builder().
            optionGroup(optionGroup1).build());
        product1.addProductOptionGroup(ProductOptionGroup.builder().
            optionGroup(optionGroup2).build());

        product2.addProductOptionGroup(ProductOptionGroup.builder().
            optionGroup(optionGroup1).build());

        given(productRepository.findAllByMemberId(memberId)).willReturn(
            List.of(product1, product2));

        // when
        List<ProductGetResponse> result = productService.getProducts(memberId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo(productName1);
        assertThat(result.getLast().name()).isEqualTo(productName2);
        assertThat(result.getFirst().optionGroups()).isEqualTo(optionGroupIds);
        assertThat(result.getLast().optionGroups()).containsExactly(optionGroupIds.getFirst());
    }

    @Test
    @DisplayName("상품 정보 수정 성공")
    void updateProductSuccess() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Long categoryId = 1L;
        List<Long> optionGroupIds = List.of(1L, 2L);
        List<Long> newOptionGroupIds = List.of(3L, 4L);

        String productName = "불고기버거";
        String productDescription = "특제 불고기 소스로 입맛 돋우는 부드러운 버거";
        int price = 6000;
        ProductState productState = ProductState.AVAILABLE;
        String picture = "pic.jpg";

        String newProductName = "치즈버거";
        String newProductDescription = "두 장의 육즙 가득한 패티와 고소한 치즈 두 장이 어우러진 풍성한 버거";
        int newPrice = 5000;
        ProductState newProductState = ProductState.AVAILABLE;
        String newPicture = "new_pic.jpg";

        ProductUpdateRequest request = new ProductUpdateRequest(categoryId, newOptionGroupIds,
            newProductName, newProductDescription, newPrice, newProductState, newPicture);

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Category category = Category.builder().member(member).build();
        ReflectionTestUtils.setField(category, "id", categoryId);

        OptionGroup optionGroup1 = OptionGroup.builder().build();
        OptionGroup optionGroup2 = OptionGroup.builder().build();
        ReflectionTestUtils.setField(optionGroup1, "id", optionGroupIds.getFirst());
        ReflectionTestUtils.setField(optionGroup2, "id", optionGroupIds.getLast());

        List<OptionGroup> optionGroups = List.of(optionGroup1, optionGroup2);
        List<ProductOptionGroup> productOptionGroups = new ArrayList<>(optionGroups.stream()
            .map(ProductOptionGroup::new)
            .toList());

        OptionGroup newOptionGroup1 = OptionGroup.builder().build();
        OptionGroup newOptionGroup2 = OptionGroup.builder().build();
        ReflectionTestUtils.setField(newOptionGroup1, "id", newOptionGroupIds.getFirst());
        ReflectionTestUtils.setField(newOptionGroup2, "id", newOptionGroupIds.getLast());
        List<OptionGroup> newOptionGroups = List.of(newOptionGroup1, newOptionGroup2);

        Product product = Product.builder()
            .member(member)
            .category(category)
            .productOptionGroups(productOptionGroups)
            .name(productName)
            .description(productDescription)
            .price(price)
            .state(productState)
            .picture(picture)
            .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(categoryService.findCategoryById(memberId, categoryId)).willReturn(category);
        given(optionGroupService.findAllOptionGroupById(memberId, newOptionGroupIds)).willReturn(
            newOptionGroups);
        given(productRepository.save(any()))
            .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        ProductUpdateResponse response = productService.updateProduct(memberId, productId, request);

        // then
        assertThat(response.categoryId()).isEqualTo(categoryId);
        assertThat(response.optionGroups()).isEqualTo(newOptionGroupIds);
        assertThat(response.name()).isEqualTo(newProductName);
        assertThat(response.description()).isEqualTo(newProductDescription);
        assertThat(response.price()).isEqualTo(newPrice);
        assertThat(response.state()).isEqualTo(newProductState);
        assertThat(response.picture()).isEqualTo(newPicture);

        then(productOptionGroupRepository).should().saveAll(any());
        then(productRepository).should().save(any());
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProductSuccess() {
        // given
        Long memberId = 1L;
        Long productId = 1L;

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Product product = Product.builder()
            .member(member)
            .build();
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(memberId, productId);

        // then
        then(productRepository).should().delete(product);
    }

    @Test
    @DisplayName("상품 조회 성공")
    void findProductByIdSuccess() {
        // given
        Long memberId = 1L;
        Long productId = 1L;

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Product product = Product.builder()
            .member(member)
            .build();
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        Product found = productService.findProductById(memberId, productId);

        // then
        assertThat(found).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 조회 실패 - 존재하지 않음")
    void findProductByIdNotFound() {
        Long memberId = 1L;
        Long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.findProductById(memberId, productId))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("상품 조회 실패 - 소유자 아님")
    void findProductByIdNotOwned() {
        Long ownerId = 1L;
        Long memberId = 2L;
        Long productId = 1L;

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", ownerId);

        Product product = Product.builder()
            .member(member)
            .build();
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> productService.findProductById(memberId, productId))
            .isInstanceOf(NotOwnedProductException.class);
    }
}