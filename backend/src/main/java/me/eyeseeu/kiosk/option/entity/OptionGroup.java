package me.eyeseeu.kiosk.option.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import me.eyeseeu.kiosk.common.entity.BaseEntity;
import me.eyeseeu.kiosk.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minCount;

    @Column(nullable = false)
    private int maxCount;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @Builder
    public OptionGroup(String name, Member member, int minCount, int maxCount) {
        this.name = name;
        this.member = member;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void update(String name, int minCount, int maxCount) {
        this.name = name;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public void clearOptions() {
        this.options.clear();
    }

}
