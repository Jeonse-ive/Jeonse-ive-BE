package com.ayu.realty.member.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.MemberSuccessCode;
import com.ayu.realty.member.model.request.MemberReq;
import com.ayu.realty.member.service.MemberService;
import com.ayu.realty.member.model.request.MemberSaveReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;


    @Operation(
            summary = "회원가입",
            description = "사용자를 등록(이메일, 비밀번호, 닉네임)")
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<Void>> signup(@RequestBody MemberSaveReq request){
        boolean isSuccess = memberService.signup(request);
        if (isSuccess) {
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_CREATED, null));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiRes.fail(ErrorCode.DUPLICATE_EMAIL));
        }
    }

    @Operation(
            summary = "전체 회원 조회",
            description = "모든 회원 정보를 조회합니다."
    )
    @GetMapping("/members")
    public ResponseEntity<ApiRes<List<MemberReq>>> getAllMembers() {

        List<MemberReq> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.GET_ALL_MEMBERS, members));
    }





}
