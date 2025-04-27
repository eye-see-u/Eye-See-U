package me.eyeseeu.kiosk.category.repository;

import java.util.List;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMember(Member member);
}
