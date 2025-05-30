package com.ayu.realty.member.service;

import com.ayu.realty.member.model.entity.Member;
import com.ayu.realty.member.model.request.MemberSignupReq;
import com.ayu.realty.member.model.response.MemberAdminRes;
import com.ayu.realty.member.model.response.MemberPublicRes;
import com.ayu.realty.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(MemberSignupReq request) {

        // 이메일 중복 체크 로직
        boolean isEmailExists = memberRepository.existsByEmail(request.getEmail());

        if (isEmailExists) {
            return false;
        }

        Member entity = Member.toEntity(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword()),// 비밀번호는 암호화하지 않고 저장
                request.getRole()
        );

        memberRepository.save(entity);
        return true;
    }

    // 모든 회원 정보를 조회하는 메서드(관리자)
    public List<MemberAdminRes> getAllMembersForAdmin() {
        return memberRepository.findAll().stream()
                .map(Member::toAdminDTO)
                .toList();
    }

    // 모든 회원 정보를 조회하는 메서드(일반 사용자)
    public List<MemberPublicRes> getAllMemberForMember() {
        return memberRepository.findAll().stream()
                .map(Member::toPublicDTO)
                .toList();
    }

    // 특정 회원의 정보를 ID로 조회하는 메서드
    public MemberPublicRes getMemberById(Long id) {
       Member member = memberRepository.findById(id)
               .orElseThrow(()-> new UsernameNotFoundException("Member not found with id: " + id));
       return Member.toPublicDTO(member);
    }

}
