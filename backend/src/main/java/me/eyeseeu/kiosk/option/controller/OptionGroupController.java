package me.eyeseeu.kiosk.option.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupUpdateRequest;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupCreateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupUpdateResponse;
import me.eyeseeu.kiosk.option.service.OptionGroupService;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/option-groups")
public class OptionGroupController {

    private final OptionGroupService optionGroupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OptionGroupCreateResponse createOptionGroup(
        @Valid @RequestBody OptionGroupCreateRequest request, HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        return optionGroupService.createOptionGroup(memberId, request);
    }

    @GetMapping
    public List<OptionGroupGetResponse> findAllOptionGroups(HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        return optionGroupService.getAllOptionGroups(memberId);
    }

    @PutMapping("/{optionGroupId}")
    public OptionGroupUpdateResponse updateOptionGroup(
        @PathVariable Long optionGroupId,
        @Valid @RequestBody OptionGroupUpdateRequest request,
        @SessionAttribute("memberId") Long memberId
    ) {
        return optionGroupService.updateOptionGroup(memberId, optionGroupId, request);
    }
}
