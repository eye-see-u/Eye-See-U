package me.eyeseeu.kiosk.option.service;

import static java.util.stream.Collectors.toList;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.service.MemberService;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupCreateRequest;
import me.eyeseeu.kiosk.option.dto.request.OptionGroupUpdateRequest;
import me.eyeseeu.kiosk.option.dto.response.OptionCreateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGetResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupCreateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupGetResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionGroupUpdateResponse;
import me.eyeseeu.kiosk.option.dto.response.OptionUpdateResponse;
import me.eyeseeu.kiosk.option.entity.Option;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import me.eyeseeu.kiosk.option.exception.NotOwnedOptionGroupException;
import me.eyeseeu.kiosk.option.exception.OptionGroupNotFoundException;
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

    @Transactional
    public OptionGroupUpdateResponse updateOptionGroup(Long memberId, Long optionGroupId,
        OptionGroupUpdateRequest request) {
        OptionGroup optionGroup = findOptionGroupById(memberId, optionGroupId);

        optionGroup.update(request.name(), request.minCount(), request.maxCount());

        optionGroup.clearOptions();
        request.options().stream()
            .map(opt -> Option.builder()
                .name(opt.name())
                .price(opt.price())
                .picture(opt.picture())
                .optionGroup(optionGroup)
                .build())
            .forEach(optionGroup::addOption);

        optionGroupRepository.save(optionGroup);

        return new OptionGroupUpdateResponse(
            optionGroup.getId(),
            optionGroup.getName(),
            optionGroup.getMinCount(),
            optionGroup.getMaxCount(),
            optionGroup.getOptions().stream()
                .map(opt -> new OptionUpdateResponse(
                    opt.getId(),
                    opt.getName(),
                    opt.getPrice(),
                    opt.getPicture()
                ))
                .collect(toList())
        );
    }

    @Transactional
    public void deleteOptionGroup(Long memberId, Long optionGroupId) {
        OptionGroup optionGroup = findOptionGroupById(memberId, optionGroupId);

        optionGroupRepository.delete(optionGroup);
    }

    public OptionGroup findOptionGroupById(Long memberId, Long optionGroupId) {
        OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
            .orElseThrow(OptionGroupNotFoundException::new);

        if (!optionGroup.getMember().getId().equals(memberId)) {
            throw new NotOwnedOptionGroupException();
        }

        return optionGroup;
    }

    public List<OptionGroup> findAllOptionGroupById(Long memberId, List<Long> idList) {
        List<OptionGroup> optionGroups = optionGroupRepository.findAllById(idList);

        if (optionGroups.size() != idList.size()) {
            throw new OptionGroupNotFoundException();
        }

        for (OptionGroup optionGroup : optionGroups) {
            if (!optionGroup.getMember().getId().equals(memberId)) {
                throw new NotOwnedOptionGroupException();
            }
        }

        return optionGroups;
    }
}
