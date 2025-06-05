package com.ayu.realty.member.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.SuccessType.MemberSuccessCode;
import com.ayu.realty.member.model.entity.Role;
import com.ayu.realty.member.model.request.MemberUpdateReq;
import com.ayu.realty.member.model.response.MemberPrivateRes;
import com.ayu.realty.member.model.response.MemberPublicRes;
import com.ayu.realty.member.model.response.MemberUpdateRes;
import com.ayu.realty.member.security.CustomUserDetails;
import com.ayu.realty.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberApiController {

    private final MemberService memberService;



    @Operation(
            summary = "회원 정보 수정", description = "닉네임, 비밀번호, 수정"
    )

    @PutMapping("/memberInfo")
    public ResponseEntity<ApiRes<MemberUpdateRes>> updateMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody MemberUpdateReq request) throws AccessDeniedException {
         MemberUpdateRes res = memberService.updateMemberInfo(customUserDetails.getIdx(), request);
         return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_UPDATED, res));
    }

    @DeleteMapping("/")
    public ResponseEntity<ApiRes<Void>> deleteMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("password") String password) throws AccessDeniedException {
        memberService.deleteMember(customUserDetails.getIdx(), password);
        return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_DELETED));
    }


    @Operation(
            summary = "사용자 전체 회원 조회",
            description = "관리자는 모든 회원의 모든 정보를, 일반회원은 전체 회원의 정보(이메일,이름)를 조회합니다."
    )
    @GetMapping("/")
    public ResponseEntity<ApiRes<List<?>>> getAllMembers(@AuthenticationPrincipal CustomUserDetails member) {
        if(member.getRole().equals(Role.ADMIN)) {
            List<MemberPrivateRes> adminRes = memberService.getAllMembersForAdmin();
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.GET_ALL_MEMBERS, adminRes));
        } else if (member.getRole().equals(Role.USER)){
            List<MemberPublicRes> members = memberService.getAllMemberForMember();
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.GET_ALL_MEMBERS, members));
        } else {
            // 권한이 없는 경우(예: 게스트 사용자)
            return null;
        }
    }


    @Operation(
            summary = "특정 회원 정보 조회",
            description = "ID로 특정 회원의 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiRes<MemberPublicRes>> getMemberById(@PathVariable Long id) {
        MemberPublicRes dto = memberService.getMemberById(id);

        return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_VIEW, dto));
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiRes<MemberPublicRes>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberPublicRes dto = memberService.getMemberByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_VIEW, dto));
    }


}
