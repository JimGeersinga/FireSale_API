package com.firesale.api.util;

import com.firesale.api.config.MailConfig;
import com.firesale.api.model.User;
import org.springframework.mail.SimpleMailMessage;

public class MailUtil {
    public MailUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static SimpleMailMessage constructEmail(User user, String subject, String body) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setFrom(MailConfig.FROM);
        return simpleMailMessage;
    }
}
