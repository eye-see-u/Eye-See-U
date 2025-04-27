package me.eyeseeu.kiosk.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.category.dto.request.CategoryCreateRequest;
import me.eyeseeu.kiosk.category.dto.response.CategoryCreateResponse;
import me.eyeseeu.kiosk.category.dto.response.CategoryGetResponse;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.category.repository.CategoryRepository;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberService memberService;

    public CategoryCreateResponse createCategory(CategoryCreateRequest request, Long memberId) {
        Member member = memberService.findMemberById(memberId);

        Category category = Category.builder()
            .name(request.name())
            .member(member)
            .build();

        Category savedCategory = categoryRepository.save(category);

        return new CategoryCreateResponse(savedCategory.getId(), savedCategory.getName());
    }

    public List<CategoryGetResponse> getAllCategories(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        List<Category> categories = categoryRepository.findAllByMember(member);

        return categories.stream()
            .map(category -> new CategoryGetResponse(category.getId(), category.getName()))
            .toList();
    }
}
