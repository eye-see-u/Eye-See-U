package me.eyeseeu.kiosk.member.dto.response;

public record LoginResponse(
    Long memberId,
    String email,
    String name,
    String storeName
) {

}
