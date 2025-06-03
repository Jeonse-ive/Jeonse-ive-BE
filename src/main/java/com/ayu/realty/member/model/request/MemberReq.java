package com.ayu.realty.member.model.request;

import com.ayu.realty.member.model.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberReq {

    private String email;
    private String nickname;
    private String password;
    private Role role;

}
