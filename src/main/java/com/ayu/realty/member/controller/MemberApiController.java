package com.ayu.realty.member.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.MemberSuccessCode;
import com.ayu.realty.member.model.entity.Role;
import com.ayu.realty.member.model.response.MemberAdminRes;
import com.ayu.realty.member.model.response.MemberPublicRes;
import com.ayu.realty.member.security.CustomUserDetails;
import com.ayu.realty.member.service.MemberService;
import com.ayu.realty.member.model.request.MemberSignupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberApiController {

    private final MemberService memberService;


    @Operation(
            summary = "회원가입",
            description = "사용자를 등록(이메일, 비밀번호, 닉네임)")
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<Void>> signup(@Valid @RequestBody MemberSignupReq request){
        boolean isSuccess = memberService.signup(request);
        if (isSuccess) {
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_CREATED, null));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiRes.fail(ErrorCode.DUPLICATE_EMAIL));
        }
    }

    @Operation(
            summary = "사용자 전체 회원 조회",
            description = "모든 회원의 정보(이메일,이름)를 조회합니다."
    )
    @GetMapping("/members")
    public ResponseEntity<ApiRes<List<?>>> getAllMembers(@AuthenticationPrincipal CustomUserDetails member) {
        if(member.getRole().equals(Role.ADMIN)) {
            List<MemberAdminRes> adminRes = memberService.getAllMembersForAdmin();
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.GET_ALL_MEMBERS, adminRes));
        } else {
            List<MemberPublicRes> members = memberService.getAllMemberForMember();
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.GET_ALL_MEMBERS, members));
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






}
