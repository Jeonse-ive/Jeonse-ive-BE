package com.ayu.realty.member.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberSaveReq {

    private String email;
    private String password;
    private String nickname;
}
