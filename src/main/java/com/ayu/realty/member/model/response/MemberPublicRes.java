package com.ayu.realty.member.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberPublicRes {

    private String email;
    private String nickname;
}
