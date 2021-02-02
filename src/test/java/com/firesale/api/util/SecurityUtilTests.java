package com.firesale.api.util;

import com.firesale.api.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTests {
    @Test
    @DisplayName("Test security is a utility class Success")
    void isUtilityClass() {
        // Execute service call
        var exception = assertThrows(IllegalStateException.class, () ->new SecurityUtil());

        // Assert response
        assertTrue(exception.getMessage().contains("Utility class"));
    }

    @Test
    @DisplayName("Test SecurityUtil Success")
    void getSecurityContextUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserPrincipal principal = mock(UserPrincipal.class);
        when((authentication.getPrincipal())).thenReturn(principal);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Execute service call
        var userPrincipal = SecurityUtil.getSecurityContextUser();

        // Assert response
        Assertions.assertSame(principal, userPrincipal, "Incorrect userPrincipal");
    }

    @Test
    @DisplayName("Test SecurityUtil no principal Success")
    void getSecurityContextUserNoPrincipal() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when((authentication.getPrincipal())).thenReturn(null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Execute service call
        var userPrincipal = SecurityUtil.getSecurityContextUser();

        // Assert response
        Assertions.assertNull(userPrincipal, "Incorrect userPrincipal");
    }

    @Test
    @DisplayName("Test SecurityUtil no authentication Success")
    void getSecurityContextUserNoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Execute service call
        var userPrincipal = SecurityUtil.getSecurityContextUser();

        // Assert response
        Assertions.assertNull(userPrincipal, "Incorrect userPrincipal");
    }
}
