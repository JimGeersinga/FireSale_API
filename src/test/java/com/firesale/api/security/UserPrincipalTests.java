package com.firesale.api.security;

import com.firesale.api.model.Role;
import com.firesale.api.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPrincipalTests {

    @Test
    @DisplayName("Test UserPrincipal authorities Success")
    void userPrincipalGetAuthorities() {
        User user = mock(User.class);

        when(user.getRole()).thenReturn(Role.USER);


        // Execute service call
        var userPrincipal = new UserPrincipal(user);
        var authorities = userPrincipal.getAuthorities();

        // Assert response
        Assertions.assertSame(Role.USER.getAuthorities(), authorities, "Incorrect authorities");
    }

    @Test
    @DisplayName("Test UserPrincipal password Success")
    void userPrincipalGetPassword() {
        User user = mock(User.class);

        when(user.getPassword()).thenReturn("test");

        // Execute service call
        var userPrincipal = new UserPrincipal(user);
        var password = userPrincipal.getPassword();

        // Assert response
        Assertions.assertSame("test", password, "Incorrect password");
    }

    @Test
    @DisplayName("Test UserPrincipal username Success")
    void userPrincipalGetUserName() {
        User user = mock(User.class);

        when(user.getEmail()).thenReturn("test@firesale.nl");

        // Execute service call
        var userPrincipal = new UserPrincipal(user);
        var username = userPrincipal.getUsername();

        // Assert response
        Assertions.assertSame("test@firesale.nl", username, "Incorrect username");
    }

    @Test
    @DisplayName("Test UserPrincipal isAccountNonExpired Success")
    void userPrincipalIsAccountNonExpired() {
        // Execute service call
        var userPrincipal = new UserPrincipal(null);
        var value = userPrincipal.isAccountNonExpired();

        // Assert response
        Assertions.assertSame(true, value, "Incorrect isAccountNonExpired");
    }

    @Test
    @DisplayName("Test UserPrincipal isAccountNonLocked Success")
    void userPrincipalIsAccountNonLocked() {
        // Execute service call
        var userPrincipal = new UserPrincipal(null);
        var value = userPrincipal.isAccountNonLocked();

        // Assert response
        Assertions.assertSame(true, value, "Incorrect isAccountNonLocked");
    }

    @Test
    @DisplayName("Test UserPrincipal isCredentialsNonExpired Success")
    void userPrincipalIsCredentialsNonExpired() {
        // Execute service call
        var userPrincipal = new UserPrincipal(null);
        var value = userPrincipal.isCredentialsNonExpired();

        // Assert response
        Assertions.assertSame(true, value, "Incorrect isCredentialsNonExpired");
    }

    @Test
    @DisplayName("Test UserPrincipal isEnabled Success")
    void userPrincipalIsEnabled() {
        User user = mock(User.class);

        when(user.getIsLocked()).thenReturn(false);

        // Execute service call
        var userPrincipal = new UserPrincipal(user);
        var value = userPrincipal.isEnabled();

        // Assert response
        Assertions.assertSame(true, value, "Incorrect isEnabled");
    }

    @Test
    @DisplayName("Test UserPrincipal equals Success")
    void userPrincipalEquals() {
        User user = mock(User.class);
        final var a = new UserPrincipal(user);
        final var b = new UserPrincipal(user);

        Assertions.assertTrue(a.canEqual(b));
        Assertions.assertEquals(a, b);
        Assertions.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Test UserPrincipal not equals Success")
    void userPrincipalNotEquals() {
        User userA = mock(User.class);
        final var a = new UserPrincipal(userA);
        User userB = mock(User.class);
        final var b = new UserPrincipal(userB);

        Assertions.assertFalse(a.canEqual(userA));
        Assertions.assertNotEquals(a, b);
        Assertions.assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Test UserPrincipal ToString Success")
    void userPrincipalToString() {
        User user = mock(User.class);
        final var a = new UserPrincipal(user);

        Assertions.assertNotNull(a.toString());
    }
}
