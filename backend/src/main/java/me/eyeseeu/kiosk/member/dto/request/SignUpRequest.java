package me.eyeseeu.kiosk.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
        message = "비밀번호는 8자 이상이며, 영어, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    String password,

    @NotBlank(message = "이름은 필수입니다.")
    String name,

    @NotBlank(message = "매장 이름은 필수입니다.")
    String storeName
) {

}
