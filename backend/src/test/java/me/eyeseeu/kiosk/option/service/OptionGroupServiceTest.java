package me.eyeseeu.kiosk.option.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.dto.request.OptionCreateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupUpdateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionUpdateRequest;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupUpdateResponse;
import me.eyeseeu.kiosk.option.entity.Option;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.exception.NotOwnedOptionGroupException;
import me.eyeseeu.kiosk.option.exception.OptionGroupNotFoundException;
import me.eyeseeu.kiosk.option.repository.OptionGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OptionGroupServiceTest {

    @Mock
    private OptionGroupRepository optionGroupRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private OptionGroupService optionGroupService;

    @Test
    @DisplayName("옵션 그룹 생성 성공")
    void createOptionGroupSuccess() {
        // Given
        Long memberId = 1L;
        String optionGroupName = "토핑";
        String firstOptionName = "패티";

        OptionCreateRequest option1 = new OptionCreateRequest(firstOptionName, 2000, null);
        OptionCreateRequest option2 = new OptionCreateRequest("토마토", 500, "http://toamto.png");

        OptionGroupCreateRequest request = new OptionGroupCreateRequest(
            optionGroupName, 1, 5, List.of(option1, option2)
        );

        Member member = Member.builder().build();

        given(memberService.findMemberById(memberId)).willReturn(member);
        given(optionGroupRepository.save(any())).willAnswer(
            invocation -> invocation.getArgument(0));

        // When
        optionGroupService.createOptionGroup(memberId, request);

        // Then
        then(optionGroupRepository).should().save(any(OptionGroup.class));
        assertThat(request.name()).isEqualTo(optionGroupName);
        assertThat(request.options()).hasSize(2);
        assertThat(request.options().getFirst().name()).isEqualTo(firstOptionName);
    }

    @Test
    @DisplayName("옵션 그룹 목록 조회 성공")
    void getAllOptionGroupsSuccess() {
        // Given
        Long memberId = 1L;
        String firstOptionGroupName = "토핑";
        String firstOptionName = "패티";

        Member member = Member.builder().build();

        OptionGroup group1 = OptionGroup.builder()
            .name(firstOptionGroupName).minCount(1).maxCount(3)
            .member(member)
            .build();
        group1.addOption(Option.builder().name(firstOptionName).price(200).build());

        OptionGroup group2 = OptionGroup.builder()
            .name("사이드").minCount(1).maxCount(1)
            .member(member)
            .build();
        group2.addOption(Option.builder().name("감자튀김").price(0).build());

        given(memberService.findMemberById(memberId)).willReturn(member);
        given(optionGroupRepository.findAllByMember(member)).willReturn(List.of(group1, group2));

        // When
        List<OptionGroupGetResponse> response = optionGroupService.getAllOptionGroups(memberId);

        // Then
        assertThat(response).hasSize(2);
        assertThat(response.getFirst().name()).isEqualTo(firstOptionGroupName);
        assertThat(response.getFirst().options().getFirst().name()).isEqualTo(firstOptionName);
    }

    @Test
    @DisplayName("옵션 그룹 수정 성공")
    void updateOptionGroupSuccess() {
        // Given
        Long memberId = 1L;
        Long optionGroupId = 1L;
        String optionGroupName = "토핑";
        String newOptionGroupName = "사이드";
        String optionName = "패티";
        int minCount = 1;
        int maxCount = 3;
        int newMaxCount = 5;

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        OptionGroup optionGroup = OptionGroup.builder()
            .name(optionGroupName)
            .minCount(minCount)
            .maxCount(maxCount)
            .member(member)
            .build();

        ReflectionTestUtils.setField(optionGroup, "id", optionGroupId);

        OptionGroupUpdateRequest request = new OptionGroupUpdateRequest(
            newOptionGroupName,
            minCount,
            newMaxCount,
            List.of(
                new OptionUpdateRequest(optionName, 2000, "https://patty.png"),
                new OptionUpdateRequest("토마토", 500, "https://tomato.png"),
                new OptionUpdateRequest("베이컨", 1000, null)
            )
        );

        given(optionGroupRepository.findById(optionGroupId)).willReturn(Optional.of(optionGroup));

        // When
        OptionGroupUpdateResponse response = optionGroupService.updateOptionGroup(memberId,
            optionGroupId, request);

        // Then
        assertThat(response.id()).isEqualTo(optionGroupId);
        assertThat(response.name()).isEqualTo(newOptionGroupName);
        assertThat(response.minCount()).isEqualTo(minCount);
        assertThat(response.maxCount()).isEqualTo(newMaxCount);
        assertThat(response.options()).hasSize(3);
        assertThat(response.options().getFirst().name()).isEqualTo(optionName);
    }

    @Test
    @DisplayName("옵션 그룹 수정 실패 - 존재하지 않는 옵션 그룹")
    void updateOptionGroupNotFound() {
        // Given
        Long memberId = 1L;
        Long optionGroupId = 1L;

        OptionGroupUpdateRequest request = new OptionGroupUpdateRequest(
            "이름",
            1,
            2,
            List.of()
        );

        given(optionGroupRepository.findById(optionGroupId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(
            () -> optionGroupService.updateOptionGroup(memberId, optionGroupId, request))
            .isInstanceOf(OptionGroupNotFoundException.class);
    }

    @Test
    @DisplayName("옵션 그룹 수정 실패 - 권한 없음")
    void updateOptionGroupNotOwned() {
        // Given
        Long requestMemberId = 1L;
        Long ownerId = 2L;
        Long optionGroupId = 1L;

        Member owner = Member.builder().build();
        ReflectionTestUtils.setField(owner, "id", ownerId);

        OptionGroup optionGroup = OptionGroup.builder()
            .name("옵션그룹")
            .minCount(1)
            .maxCount(3)
            .member(owner)
            .build();

        given(optionGroupRepository.findById(optionGroupId)).willReturn(Optional.of(optionGroup));

        OptionGroupUpdateRequest request = new OptionGroupUpdateRequest("새 이름", 0, 1, List.of());

        // When & Then
        assertThatThrownBy(
            () -> optionGroupService.updateOptionGroup(requestMemberId, optionGroupId, request))
            .isInstanceOf(NotOwnedOptionGroupException.class);
    }
}