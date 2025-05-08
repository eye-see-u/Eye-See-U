package me.eyeseeu.kiosk.option.service;

import static java.util.stream.Collectors.toList;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupCreateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupCreateResponse.OptionCreateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse.OptionGetResponse;
import me.eyeseeu.kiosk.option.entity.Option;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.repository.OptionGroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionGroupService {

    private final OptionGroupRepository optionGroupRepository;
    private final MemberService memberService;

    @Transactional
    public OptionGroupCreateResponse createOptionGroup(Long memberId,
        OptionGroupCreateRequest request) {
        Member member = memberService.findMemberById(memberId);

        OptionGroup optionGroup = OptionGroup.builder()
            .member(member)
            .name(request.name())
            .minCount(request.minCount())
            .maxCount(request.maxCount())
            .build();

        request.options().stream()
            .map(optionCreateRequest -> Option.builder()
                .name(optionCreateRequest.name())
                .price(optionCreateRequest.price())
                .picture(optionCreateRequest.picture())
                .optionGroup(optionGroup)
                .build())
            .forEach(optionGroup::addOption);

        OptionGroup savedGroup = optionGroupRepository.save(optionGroup);

        return new OptionGroupCreateResponse(
            savedGroup.getId(),
            savedGroup.getName(),
            savedGroup.getMinCount(),
            savedGroup.getMaxCount(),
            savedGroup.getOptions().stream()
                .map(option -> new OptionCreateResponse(
                    option.getId(),
                    option.getName(),
                    option.getPrice(),
                    option.getPicture()
                ))
                .collect(toList())
        );
    }

    public List<OptionGroupGetResponse> getAllOptionGroups(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<OptionGroup> optionGroups = optionGroupRepository.findAllByMember(member);

        return optionGroups.stream()
            .map(optionGroup -> {
                List<Option> options = optionGroup.getOptions();

                return new OptionGroupGetResponse(
                    optionGroup.getId(),
                    optionGroup.getName(),
                    optionGroup.getMinCount(),
                    optionGroup.getMaxCount(),
                    options.stream()
                        .map(opt -> new OptionGetResponse(
                            opt.getId(),
                            opt.getName(),
                            opt.getPrice(),
                            opt.getPicture()
                        ))
                        .collect(toList())
                );
            })
            .collect(toList());
    }

}
