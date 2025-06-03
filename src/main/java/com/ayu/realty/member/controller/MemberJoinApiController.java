package com.ayu.realty.member.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.MemberSuccessCode;
import com.ayu.realty.member.model.request.MemberSignupReq;
import com.ayu.realty.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberJoinApiController {

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
}
