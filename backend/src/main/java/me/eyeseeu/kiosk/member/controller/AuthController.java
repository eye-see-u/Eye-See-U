package me.eyeseeu.kiosk.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.eyeseeu.kiosk.member.dto.request.SignUpRequest;
import me.eyeseeu.kiosk.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.signup(signUpRequest);
    }
}
