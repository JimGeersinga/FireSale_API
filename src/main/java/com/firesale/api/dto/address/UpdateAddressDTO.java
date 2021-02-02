package com.firesale.api.dto.address;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UpdateAddressDTO {
    @NotEmpty(message = "Street must have a value")
    private String street;

    @NotEmpty(message = "House number must have a value")
    private String houseNumber;

    @NotEmpty(message = "Postal code must have a value")
    @Pattern(regexp = "[0-9]{4}(\s?)[a-zA-Z]{2}", message = "Postal code is not valid")
    private String postalCode;

    @NotEmpty(message = "City must have a value")
    private String city;

    @NotEmpty(message = "Country must have a value")
    private String country;
}
