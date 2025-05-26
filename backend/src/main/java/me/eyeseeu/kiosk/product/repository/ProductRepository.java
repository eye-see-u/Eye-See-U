package me.eyeseeu.kiosk.product.repository;

import java.util.List;
import me.eyeseeu.kiosk.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.member.id = :memberId")
    List<Product> findAllByMemberId(Long memberId);
}
