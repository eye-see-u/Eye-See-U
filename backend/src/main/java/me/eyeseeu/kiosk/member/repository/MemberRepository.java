package me.eyeseeu.kiosk.member.repository;

import me.eyeseeu.kiosk.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
}
