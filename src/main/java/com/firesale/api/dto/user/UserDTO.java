package com.firesale.api.dto.user;

import com.firesale.api.dto.BaseDTO;
import com.firesale.api.model.Gender;
import com.firesale.api.model.Role;
import com.firesale.api.dto.address.AddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO extends BaseDTO {
    private String email;
    private String displayName;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Gender gender;
    private Role role;
    private AddressDTO address;
    private AddressDTO shippingAddress;
    private byte[] avatar;
}
