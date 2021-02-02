package com.firesale.api.service;

import com.firesale.api.exception.InvalidResetTokenException;
import com.firesale.api.model.ErrorTypes;
import com.firesale.api.model.PasswordResetToken;
import com.firesale.api.model.User;
import com.firesale.api.repository.PasswordResetTokenRepository;
import com.firesale.api.repository.UserRepository;
import com.firesale.api.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;
    private final JavaMailSender mailSender;

    @Transactional(readOnly = false)
    public void createPasswordResetTokenForUser(String email) {
        final User user = userService.findUserByEmail(email);
        final UUID token = UUID.randomUUID();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        final String subject = "Wachtwoord reset - firesale";
        final String body = "Gebruik de volgende link om uw wachtwoord te resetten: http://localhost:4200/users/changepassword/" + myToken.getToken();
        passwordResetTokenRepository.save(myToken);
        var message = MailUtil.constructEmail(user, subject, body);
        mailSender.send(message);
    }

    @Transactional(readOnly = false)
    public void changeUserPassword(String token, String password) {

        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        if (passToken == null) {
            throw new InvalidResetTokenException("Password reset token is invalid", ErrorTypes.INVALID_RESET_TOKEN);
        } else if (passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidResetTokenException("Password reset token is expired", ErrorTypes.EXPIRED_RESET_TOKEN);
        }
        var user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        passwordResetTokenRepository.delete(passwordResetTokenRepository.findByUser(user));
        userRepository.save(user);
    }
}
