package com.firesale.api.dto.user;

import com.firesale.api.dto.address.AddressDTO;
import lombok.Data;

@Data
public class PersonInfoDTO {
    private String name;
    private String email;
    private AddressDTO address;
}