package com.firesale.api.security;

import com.firesale.api.model.Role;
import com.firesale.api.util.SecurityUtil;

public class Guard {
    public Guard() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isAdmin() {
        var contextUser = SecurityUtil.getSecurityContextUser();
        if(contextUser == null) return  false;
        return contextUser.getUser().getRole() == Role.ADMIN;
    }

    public static boolean isSelf(final Long id) {
        var contextUser = SecurityUtil.getSecurityContextUser();
        if(contextUser == null) return  false;
        return contextUser.getUser().getId().equals(id);
    }
}
