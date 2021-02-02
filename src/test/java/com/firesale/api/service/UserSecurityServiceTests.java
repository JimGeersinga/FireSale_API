package com.firesale.api.service;

import com.firesale.api.exception.InvalidResetTokenException;
import com.firesale.api.model.PasswordResetToken;
import com.firesale.api.model.User;
import com.firesale.api.repository.PasswordResetTokenRepository;
import com.firesale.api.repository.UserRepository;
import com.firesale.api.util.MailUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @InjectMocks
    private UserSecurityService userSecurityService;

    @Test
    @DisplayName("Test createPasswordResetTokenForUser Success")
    void createPasswordResetTokenForUser() {
        // Setup mock repository
        String email = "test@test.nl";
        try(MockedStatic<MailUtil> dummy = Mockito.mockStatic(MailUtil.class)){
            doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
            doReturn(new User()).when(userService).findUserByEmail(email);
            when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer((answer) -> answer.getArguments()[0]);
            dummy.when(() -> MailUtil.constructEmail(any(User.class), any(String.class), any(String.class))).thenReturn(new SimpleMailMessage());
            // Execute service call
            userSecurityService.createPasswordResetTokenForUser(email);
            // Assert response
            verify(userService).findUserByEmail(email);
            dummy.verify(() -> MailUtil.constructEmail(any(User.class), any(String.class), any(String.class)));
        }
    }

    @Test
    @DisplayName("Test changeUserPassword notFound Failure")
    void changeUserPassword() {
        // Setup mock repository
        doReturn(null).when(passwordResetTokenRepository).findByToken(any(String.class));
        String expected = "Password reset token is invalid: [Password reset token is invalid]";
        // Execute service call
        var returned = Assertions.assertThrows(InvalidResetTokenException.class, () ->userSecurityService.changeUserPassword("test", "test"));
        String actual = returned.getMessage();
        // Assert response
        verify(passwordResetTokenRepository).findByToken(any(String.class));
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test changeUserPassword expired Failure")
    void changeUserPasswordFailure() {
        // Setup mock repository
        var t = UUID.randomUUID();
        PasswordResetToken token = new PasswordResetToken(t, new User());
        token.setExpiryDate(LocalDateTime.of(1995, 12, 1,10,20));
        doReturn(token).when(passwordResetTokenRepository).findByToken(any(String.class));
        String expected = "Password reset token is invalid: [Password reset token is expired]";
        // Execute service call
        var returned = Assertions.assertThrows(InvalidResetTokenException.class, () ->userSecurityService.changeUserPassword("test", "test"));
        String actual = returned.getMessage();
        // Assert response
        verify(passwordResetTokenRepository).findByToken(any(String.class));
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test changeUserPassword Success")
    void changeUserPasswordSuccess() {
        // Setup mock repository
        var t = UUID.randomUUID();
        PasswordResetToken token = new PasswordResetToken(t, new User());
        token.setExpiryDate(LocalDateTime.of(3000, 12, 1,10,20));
        doReturn(token).when(passwordResetTokenRepository).findByToken(any(String.class));
        doNothing().when(passwordResetTokenRepository).delete(any(PasswordResetToken.class));
        doReturn(token).when(passwordResetTokenRepository).findByUser(any(User.class));
        doReturn("").when(passwordEncoder).encode(any(String.class));
        when(userRepository.save(any(User.class))).thenAnswer((answer) -> answer.getArguments()[0]);

        // Execute service call
        userSecurityService.changeUserPassword("test", "test");
        // Assert response
        verify(passwordResetTokenRepository).findByToken(any(String.class));
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(any(String.class));
        verify(passwordResetTokenRepository).delete(any(PasswordResetToken.class));
        verify(passwordResetTokenRepository).findByUser(any(User.class));
    }
}
