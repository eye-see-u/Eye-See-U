package me.eyeseeu.kiosk.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import me.eyeseeu.kiosk.category.dto.request.CategoryCreateRequest;
import me.eyeseeu.kiosk.category.dto.response.CategoryCreateResponse;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.repository.CategoryRepository;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 성공")
    public void createCategorySuccess(){
        // Given
        Long memberId = 1L;
        String categoryName = "Test Category";
        CategoryCreateRequest request = new CategoryCreateRequest(categoryName);

        Member member = Member.builder().build();
        Category category = Category.builder()
            .name(categoryName)
            .member(member)
            .build();

        given(memberService.findMemberById(memberId)).willReturn(Member.builder().build());
        given(categoryRepository.save(any())).willReturn(category);

        // When
        CategoryCreateResponse response = categoryService.createCategory(request, memberId);

        // Then
        assertThat(response.name()).isEqualTo(categoryName);

    }

}