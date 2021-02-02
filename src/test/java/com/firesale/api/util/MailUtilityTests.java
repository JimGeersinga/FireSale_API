package com.firesale.api.util;

import com.firesale.api.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailUtilityTests {
    @Test
    @DisplayName("Test security is a utility class Success")
    void isUtilityClass() {
        // Execute service call
        var exception = assertThrows(IllegalStateException.class, () ->new MailUtil());

        // Assert response
        assertTrue(exception.getMessage().contains("Utility class"));
    }

    @Test
    @DisplayName("Test constructEmail Success")
    void constructEmail() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("test@firesale.nl");

        // Execute service call
        var message = MailUtil.constructEmail(user, "subject", "body");

        // Assert response
        Assertions.assertSame("subject", message.getSubject(), "Incorrect subject");
        Assertions.assertSame("body", message.getText(), "Incorrect body");
    }
}
