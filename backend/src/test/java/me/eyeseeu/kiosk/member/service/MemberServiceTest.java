package me.eyeseeu.kiosk.member.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import me.eyeseeu.kiosk.member.dto.request.SignUpRequest;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.exception.DuplicateEmailException;
import me.eyeseeu.kiosk.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void signup() {
        // Given
        String email = "admin@emil.com";
        String name = "admin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String storeName = "store";
        SignUpRequest signUpRequest = new SignUpRequest(email, password, name, storeName);

        given(memberRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(memberRepository.save(any())).willReturn(Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .storeName(storeName)
            .build());

        // When
        memberService.signup(signUpRequest);

        // Then
        then(memberRepository).should().existsByEmail(email);
        then(passwordEncoder).should().encode(password);
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 실패")
    void signupDuplicatedEmail() {
        // Given
        String email = "admin@emil.com";
        String name = "admin";
        String password = "password";
        String storeName = "store";
        SignUpRequest signUpRequest = new SignUpRequest(email, password, name, storeName);

        given(memberRepository.existsByEmail(email)).willReturn(true);

        // When
        // Then
        assertThrows(DuplicateEmailException.class, () -> memberService.signup(signUpRequest));
    }
}