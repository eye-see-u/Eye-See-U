package me.eyeseeu.kiosk.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import me.eyeseeu.kiosk.member.dto.request.LoginRequest;
import me.eyeseeu.kiosk.member.dto.request.SignUpRequest;
import me.eyeseeu.kiosk.member.dto.response.LoginResponse;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.exception.DeletedMemberException;
import me.eyeseeu.kiosk.member.exception.DuplicateEmailException;
import me.eyeseeu.kiosk.member.exception.InvalidCredentialsException;
import me.eyeseeu.kiosk.member.exception.MemberNotFoundException;
import me.eyeseeu.kiosk.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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
    @DisplayName("회원가입 실패 - 중복된 이메일")
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

    @Test
    @DisplayName("로그인 성공")
    void login() {
        // Given
        Long memberId = 1L;
        String email = "admin@emil.com";
        String name = "admin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String storeName = "store";

        Member member = Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .storeName(storeName)
            .build();

        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);

        // When
        LoginResponse loginResponse = memberService.login(new LoginRequest(email, password));

        // Then
        then(memberRepository).should().findByEmail(email);
        then(passwordEncoder).should().matches(password, encodedPassword);

        assertThat(loginResponse.memberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("로그인 실패 - 등록되지 않은 이메일")
    void loginFailUnregisteredEmail() {
        // Given
        Long memberId = 1L;
        String email = "admin@emil.com";
        String password = "password";

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // When
        // Then
        assertThrows(InvalidCredentialsException.class,
            () -> memberService.login(new LoginRequest(email, password)));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void loginFailWrongPassword() {
        // Given
        Long memberId = 1L;
        String email = "admin@emil.com";
        String name = "admin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String storeName = "store";

        Member member = Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .storeName(storeName)
            .build();

        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(false);

        // When
        // Then
        assertThrows(InvalidCredentialsException.class,
            () -> memberService.login(new LoginRequest(email, password)));
    }

    @Test
    @DisplayName("회원 검색 성공")
    void findMemberById() {
        // Given
        Long memberId = 1L;
        String email = "admin@emil.com";
        String name = "admin";
        String encodedPassword = "encodedPassword";
        String storeName = "store";

        Member member = Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .storeName(storeName)
            .build();
        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        Member foundMember = memberService.findMemberById(memberId);

        // Then
        then(memberRepository).should().findById(memberId);
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 검색 실패 - 존재하지 않는 회원")
    void findMemberByIdFailMemberNotFound() {
        // Given
        Long memberId = 1L;

        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // When
        // Then
        assertThrows(MemberNotFoundException.class, () -> memberService.findMemberById(memberId));
    }

    @Test
    @DisplayName("회원 검색 실패 - 삭제된 회원")
    void findMemberByIdFailDeletedMember() {
        // Given
        Long memberId = 1L;
        String email = "admin@emil.com";
        String name = "admin";
        String encodedPassword = "encodedPassword";
        String storeName = "store";

        Member member = Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .storeName(storeName)
            .build();
        ReflectionTestUtils.setField(member, "id", memberId);

        member.delete();

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        // Then
        assertThrows(DeletedMemberException.class, () -> memberService.findMemberById(memberId));
    }
}