package com.ayu.realty.global.security.jwt.filter;

import com.ayu.realty.global.util.JWTUtil;
import com.ayu.realty.member.model.entity.Member;
import com.ayu.realty.member.repository.MemberRepository;
import com.ayu.realty.member.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request Authorization 헤더를 찾는다.
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료(필수)
            return;
        }

        System.out.println("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        System.out.println("token : " + token);

        // 토큰 유효성 검증
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            Member member= memberRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("사용자 없음: " + email));

            CustomUserDetails userDetails = new CustomUserDetails(member);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
