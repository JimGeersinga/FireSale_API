package com.firesale.api.dto.usersecurity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class EmailaddressDTO {
    @NotEmpty(message = "Email must have a value")
    @Email(message = "Email should be valid")
    private String emailaddress;
}
