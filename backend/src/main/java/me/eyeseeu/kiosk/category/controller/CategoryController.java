package me.eyeseeu.kiosk.category.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.category.dto.request.CategoryCreateRequest;
import me.eyeseeu.kiosk.category.dto.request.CategoryUpdateRequest;
import me.eyeseeu.kiosk.category.dto.response.CategoryCreateResponse;
import me.eyeseeu.kiosk.category.dto.response.CategoryGetResponse;
import me.eyeseeu.kiosk.category.dto.response.CategoryUpdateResponse;
import me.eyeseeu.kiosk.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryCreateResponse createCategory(
        @Valid @RequestBody CategoryCreateRequest request, HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        return categoryService.createCategory(request, memberId);
    }

    @GetMapping
    public List<CategoryGetResponse> getCategories(HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        return categoryService.getAllCategories(memberId);
    }

    @PutMapping("/{categoryId}")
    public CategoryUpdateResponse updateCategory(
        @PathVariable Long categoryId, @Valid @RequestBody CategoryUpdateRequest request,
        HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        return categoryService.updateCategory(memberId, categoryId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(
        @PathVariable Long categoryId, HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        categoryService.deleteCategory(memberId, categoryId);
    }

}
