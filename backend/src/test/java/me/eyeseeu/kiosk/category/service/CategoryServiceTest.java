package me.eyeseeu.kiosk.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;
import me.eyeseeu.kiosk.category.dto.request.CategoryCreateRequest;
import me.eyeseeu.kiosk.category.dto.request.CategoryUpdateRequest;
import me.eyeseeu.kiosk.category.dto.response.CategoryCreateResponse;
import me.eyeseeu.kiosk.category.dto.response.CategoryGetResponse;
import me.eyeseeu.kiosk.category.dto.response.CategoryUpdateResponse;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.exception.CategoryNotFoundException;
import me.eyeseeu.kiosk.category.exception.NotOwnedCategoryException;
import me.eyeseeu.kiosk.category.repository.CategoryRepository;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    public void createCategorySuccess() {
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

    @Test
    @DisplayName("카테고리 목록 조회 성공")
    public void getAllCategoriesSuccess() {
        // Given
        Long memberId = 1L;
        Member member = Member.builder().build();
        Category category1 = Category.builder()
            .name("Category 1")
            .member(member)
            .build();
        Category category2 = Category.builder()
            .name("Category 2")
            .member(member)
            .build();

        given(memberService.findMemberById(memberId)).willReturn(member);
        given(categoryRepository.findAllByMember(member)).willReturn(List.of(category1, category2));

        // When
        List<CategoryGetResponse> response = categoryService.getAllCategories(memberId);

        // Then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).name()).isEqualTo("Category 1");
        assertThat(response.get(1).name()).isEqualTo("Category 2");
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    public void updateCategorySuccess() {
        // Given
        Long memberId = 1L;
        Long categoryId = 1L;
        String updatedName = "Updated Category";
        CategoryUpdateRequest request = new CategoryUpdateRequest(updatedName);

        Member member = Member.builder().build();
        Category category = Category.builder()
            .name("Old Category")
            .member(member)
            .build();

        ReflectionTestUtils.setField(member, "id", memberId);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // When
        CategoryUpdateResponse response = categoryService.updateCategory(memberId, categoryId,
            request);

        // Then
        assertThat(response.name()).isEqualTo(updatedName);
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    public void deleteCategorySuccess() {
        // Given
        Long memberId = 1L;
        Long categoryId = 1L;

        Member member = Member.builder().build();
        Category category = Category.builder()
            .name("Category to be deleted")
            .member(member)
            .build();

        ReflectionTestUtils.setField(member, "id", memberId);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // When
        categoryService.deleteCategory(memberId, categoryId);

        // Then
        then(categoryRepository).should().delete(category);
    }

    @Test
    @DisplayName("카테고리 검색 성공")
    public void findCategoryByIdSuccess() {
        // Given
        Long memberId = 1L;
        Long categoryId = 1L;

        Member member = Member.builder().build();
        Category category = Category.builder()
            .name("Category to be found")
            .member(member)
            .build();

        ReflectionTestUtils.setField(member, "id", memberId);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // When
        Category foundCategory = categoryService.findCategoryById(memberId, categoryId);

        // Then
        assertThat(foundCategory).isEqualTo(category);
    }

    @Test
    @DisplayName("카테고리 검색 실패 - 카테고리 없음")
    public void findCategoryByIdNotFound() {
        // Given
        Long memberId = 1L;
        Long categoryId = 1L;

        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> categoryService.findCategoryById(memberId, categoryId))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("카테고리 검색 실패 - 소유하지 않은 카테고리")
    public void findCategoryByIdNotOwned() {
        // Given
        Long ownerId = 1L;
        Long memberId = 2L;
        Long categoryId = 1L;

        Member member = Member.builder().build();
        Category category = Category.builder()
            .name("Category to be found")
            .member(member)
            .build();

        ReflectionTestUtils.setField(member, "id", ownerId);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // When
        // Then
        assertThatThrownBy(() -> categoryService.findCategoryById(memberId, categoryId))
            .isInstanceOf(NotOwnedCategoryException.class);
    }

}