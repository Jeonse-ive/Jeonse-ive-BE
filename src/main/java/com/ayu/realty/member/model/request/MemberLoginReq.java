package com.ayu.realty.member.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginReq {
    private String email;
    private String password;
}