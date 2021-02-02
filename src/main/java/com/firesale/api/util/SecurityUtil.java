package com.firesale.api.util;

import com.firesale.api.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static UserPrincipal getSecurityContextUser() {
        var context = SecurityContextHolder.getContext();
        if(context == null) return null;

        var authentication = context.getAuthentication();
        if(authentication == null) return null;

        var principal = authentication.getPrincipal();
        if(principal == "anonymousUser") return  null;

        return (UserPrincipal) principal;
    }
}
