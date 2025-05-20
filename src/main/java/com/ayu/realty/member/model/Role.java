package com.ayu.realty.member.model;

public enum Role {
    USER, ADMIN, GUEST;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
