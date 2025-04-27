package me.eyeseeu.kiosk.member.service;

import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.member.dto.request.LoginRequest;
import me.eyeseeu.kiosk.member.dto.request.SignUpRequest;
import me.eyeseeu.kiosk.member.dto.response.LoginResponse;
import me.eyeseeu.kiosk.member.entity.Member;
import me.eyeseeu.kiosk.member.exception.DuplicateEmailException;
import me.eyeseeu.kiosk.member.exception.InvalidCredentialsException;
import me.eyeseeu.kiosk.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.email())) {
            throw new DuplicateEmailException();
        }

        memberRepository.save(Member.builder()
            .email(signUpRequest.email())
            .name(signUpRequest.name())
            .password(passwordEncoder.encode(signUpRequest.password()))
            .storeName(signUpRequest.storeName())
            .build());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return new LoginResponse(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getStoreName()
        );
    }

}
