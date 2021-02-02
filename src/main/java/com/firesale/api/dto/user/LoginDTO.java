package com.firesale.api.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginDTO {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email must have a value")
    private String email;

    @NotEmpty(message = "Password must have a value")
    private String password;
}
