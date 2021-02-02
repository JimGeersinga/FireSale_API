package com.firesale.api.dto.user;

import com.firesale.api.model.Gender;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateUserDTO {
    @NotEmpty(message = "Email must have a value")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Display name must have a value")
    private String displayName;

    @NotEmpty(message = "First name must have a value")
    private String firstName;

    @NotEmpty(message = "Last name must have a value")
    private String lastName;

    @NotNull(message = "Date of birth must have a value")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must have a value")
    private Gender gender;
}
