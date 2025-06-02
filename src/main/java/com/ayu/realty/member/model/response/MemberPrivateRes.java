package com.ayu.realty.member.model.response;

import com.ayu.realty.member.model.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberPrivateRes {
    private String email;
    private String password;
    private String nickname;
    private Role role;
}