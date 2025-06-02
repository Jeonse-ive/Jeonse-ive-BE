package com.ayu.realty.member.service;

import com.ayu.realty.member.model.entity.Member;
import com.ayu.realty.member.model.entity.Role;
import com.ayu.realty.member.model.request.MemberSaveReq;
import com.ayu.realty.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(MemberSaveReq request) {

        // 이메일 중복 체크 로직
        boolean isEmailExists = memberRepository.existsByEmail(request.getEmail());

        if (isEmailExists) {
            return false;
        }

        Member entity = Member.toEntity(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword()),// 비밀번호는 암호화하지 않고 저장
                Role.ADMIN // 기본값으로 ADMIN 권한 부여
        );

        memberRepository.save(entity);
        return true;
    }
}
