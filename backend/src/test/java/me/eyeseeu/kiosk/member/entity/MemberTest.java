package me.eyeseeu.kiosk.member.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("회원 삭제시 삭제 시간을 기록한다.")
    void delete() {
        // given
        Member member = Member.builder()
            .email("test@eyeseeu.me")
            .name("testMember")
            .password("password")
            .storeName("store")
            .build();
        assertThat(member.isDeleted()).isFalse();
        LocalDateTime beforeDelete = LocalDateTime.now();

        // when
        member.delete();

        // then
        assertThat(member.isDeleted()).isTrue();
        assertThat(member.getDeletedAt()).isBetween(beforeDelete, LocalDateTime.now());

    }
}