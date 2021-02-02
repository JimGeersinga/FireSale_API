package com.firesale.api.dto.address;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PatchAddressDTO {
    private String street;
    private String houseNumber;

    @Pattern(regexp = "[0-9]{4}(\s?)[a-zA-Z]{2}", message = "Postal code is not valid")
    private String postalCode;

    private String city;
    private String country;
}
