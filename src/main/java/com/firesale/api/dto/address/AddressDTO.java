package com.firesale.api.dto.address;

import com.firesale.api.dto.BaseDTO;
import lombok.Data;

@Data
public class AddressDTO extends BaseDTO {
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
}
