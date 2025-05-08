package me.eyeseeu.kiosk.option.repository;

import java.util.List;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

    List<OptionGroup> findByMember(Member member);
}
