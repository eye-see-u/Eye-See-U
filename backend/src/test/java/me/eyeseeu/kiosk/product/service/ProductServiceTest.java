package me.eyeseeu.kiosk.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.service.CategoryService;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.service.OptionGroupService;
import me.eyeseeu.kiosk.product.dto.request.ProductCreateRequest;
import me.eyeseeu.kiosk.product.dto.response.ProductCreateResponse;
import me.eyeseeu.kiosk.product.entity.ProductState;
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

        ProductCreateRequest request = new ProductCreateRequest(categoryId, optionGroupIds, productName, productDescription, price, productState, picture);

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
}