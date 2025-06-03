package com.ayu.realty.member.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRes {
    private String email;
    private String nickname;
}
