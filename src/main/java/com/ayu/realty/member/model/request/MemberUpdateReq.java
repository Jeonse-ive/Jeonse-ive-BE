package com.ayu.realty.member.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateReq {
    private String email;
    private String nickname;
    private String password;
}
