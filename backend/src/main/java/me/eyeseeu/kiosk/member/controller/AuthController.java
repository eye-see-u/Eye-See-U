package me.eyeseeu.kiosk.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.member.dto.request.LoginRequest;
import me.eyeseeu.kiosk.member.dto.request.SignUpRequest;
import me.eyeseeu.kiosk.member.dto.response.LoginResponse;
import me.eyeseeu.kiosk.member.dto.response.MemberInfo;
import me.eyeseeu.kiosk.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.signup(signUpRequest);
    }

    @PostMapping("/login")
    public MemberInfo login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        MemberInfo memberInfo = new MemberInfo(
            loginResponse.email(),
            loginResponse.name(),
            loginResponse.storeName()
        );

        session.setAttribute("memberId", loginResponse.memberId());
        session.setAttribute("memberInfo", memberInfo);

        return memberInfo;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/me")
    public MemberInfo getMemberInfo(HttpSession session) {
        return (MemberInfo) session.getAttribute("memberInfo");
    }
}
