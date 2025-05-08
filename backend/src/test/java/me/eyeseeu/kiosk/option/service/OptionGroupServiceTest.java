package me.eyeseeu.kiosk.option.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest.OptionCreateRequest;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse;
import me.eyeseeu.kiosk.option.entity.Option;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.repository.OptionGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}