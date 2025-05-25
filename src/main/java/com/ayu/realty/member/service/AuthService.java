package com.ayu.realty.member.service;

import com.ayu.realty.global.security.jwt.service.BlackListTokenService;
import com.ayu.realty.global.security.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final BlackListTokenService blackListTokenService;
    private final JwtTokenService jwtTokenService;

    public void logout(String accessToken) {
        // 예시: JWT 토큰을 블랙리스트에 추가하여 만료시키는 로직
        blackListTokenService.blackListTokenSave(accessToken);

        // 예시: JWT 토큰을 삭제하는 로직
        jwtTokenService.refreshTokenDelete(accessToken);

    }
}
