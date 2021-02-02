package com.firesale.api.service;

import com.firesale.api.model.User;
import com.firesale.api.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    @DisplayName("Test loadUserByUsername notFound Failure")
    void loadUserByUsername() {
        // Setup mock repository
        String email = "test@test.nl";
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);
        String expected = String.format("No user with email: %s was found", email);
        // Execute service call
        var returned = Assertions.assertThrows(UsernameNotFoundException.class, () ->userDetailService.loadUserByUsername(email));
        String actual = returned.getMessage();
        // Assert response
        verify(userRepository).findByEmail(email);
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }


    @Test
    @DisplayName("Test loadUserByUsername success")
    void loadUserByUsernameSuccess() {
        // Setup mock repository
        String email = "test@test.nl";
        User user = new User();
        user.setId(1L);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(email);
        // Execute service call
        var returned = userDetailService.loadUserByUsername(email);
        // Assert response
        verify(userRepository).findByEmail(email);
        Assertions.assertNotNull(returned, "Incorrect user");
    }
}
