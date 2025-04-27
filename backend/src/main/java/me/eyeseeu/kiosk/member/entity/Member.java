package me.eyeseeu.kiosk.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.eyeseeu.kiosk.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String storeName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime deletedAt;

    @Builder
    private Member(String email, String password, String name, String storeName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.storeName = storeName;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

}
