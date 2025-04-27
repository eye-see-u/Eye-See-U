package me.eyeseeu.kiosk.category.repository;

import me.eyeseeu.kiosk.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
