package com.example.entity.enums;

import static com.example.constant.Authority.ADMIN_AUTHORITIES;
import static com.example.constant.Authority.USER_AUTHORITIES;

public enum ERole {
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES);
    private String[] authorities;

    ERole(String[] authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
