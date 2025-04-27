package me.eyeseeu.kiosk.member.repository;

import java.util.Optional;
import me.eyeseeu.kiosk.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
