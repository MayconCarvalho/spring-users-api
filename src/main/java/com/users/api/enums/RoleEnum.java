package com.users.api.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum RoleEnum implements GrantedAuthority {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
