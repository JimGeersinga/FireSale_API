package com.firesale.api.dto.user;

import com.firesale.api.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

@Data
public class UserProfileDTO extends BaseDTO {
    private String displayName;

    private byte[] image;

    // Fix for mapstruct, mapstruct wouldn't compile when the property name is 'avatar'
    @JsonGetter("avatar")
    public byte[] getImage() {
        return image;
    }
}
