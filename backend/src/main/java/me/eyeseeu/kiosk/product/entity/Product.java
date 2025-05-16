package me.eyeseeu.kiosk.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.eyeseeu.kiosk.category.entity.Category;
import me.eyeseeu.kiosk.common.entity.BaseEntity;
import me.eyeseeu.kiosk.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionGroup> productOptionGroups = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState state;

    private String picture;

    @Builder
    public Product(Member member, Category category, String name, String description, int price,
        ProductState state, List<ProductOptionGroup> productOptionGroups, String picture) {
        this.member = member;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.productOptionGroups = productOptionGroups;
        this.picture = picture;
    }

    public void addProductOptionGroup(ProductOptionGroup productOptionGroup) {
        this.productOptionGroups.add(productOptionGroup);
        productOptionGroup.setProduct(this);
    }

    public void update(String name, String description, int price, ProductState state,
        Category category, List<ProductOptionGroup> productOptionGroups, String picture) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.category = category;
        this.productOptionGroups = productOptionGroups;
        this.picture = picture;
    }
}
