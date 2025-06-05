package com.ayu.realty.member.service;

import com.ayu.realty.global.exception.InvalidPasswordException;
import com.ayu.realty.member.model.entity.Member;
import com.ayu.realty.member.model.request.MemberSignupReq;
import com.ayu.realty.member.model.request.MemberUpdateReq;
import com.ayu.realty.member.model.response.MemberPrivateRes;
import com.ayu.realty.member.model.response.MemberPublicRes;
import com.ayu.realty.member.model.response.MemberUpdateRes;
import com.ayu.realty.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<MemberPrivateRes> getAllMembersForAdmin() {
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

    // 특정 회원의 정보를 수정
    @Transactional
    public MemberUpdateRes updateMemberInfo(Long id, MemberUpdateReq req) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with id:" + id));

        // 닉네임 변경
        if (req.getNickname() != null && !req.getNickname().isBlank()) {
            member.changeNickname(req.getNickname());
        }

        // 비밀번호가 변경된 경우에만 암호화
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            String encryptedPassword = passwordEncoder.encode(req.getPassword());
            member.changePassword(encryptedPassword);
        }

        return MemberUpdateRes.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    @Transactional
    public void deleteMember(Long id, String password) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("Member not found with id: " + id));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }

    public MemberPublicRes getMemberByEmail(String email) {
        Member entity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return Member.toPublicDTO(entity);
    }
}
