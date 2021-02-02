package com.firesale.api.dto.user;

import com.firesale.api.dto.address.UpdateAddressDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterDTO extends UpdateUserDTO {

    @NotEmpty(message = "Password must have a value")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password should have a minimum eight characters, at least one letter, one number and one special character")
    private String password;

    @NotNull(message = "Address must have a value")
    private UpdateAddressDTO address;

    private UpdateAddressDTO shippingAddress;
}
