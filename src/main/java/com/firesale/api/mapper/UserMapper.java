package com.firesale.api.mapper;

import com.firesale.api.model.Image;
import com.firesale.api.model.User;
import com.firesale.api.dto.user.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface UserMapper extends ModelToDTOMapper<User, UserDTO> {

    @Override
    @Mapping(target = "avatar", ignore = true)
    User toModel(UserDTO user);

    User toModel(RegisterDTO register);

    User toModel(PatchUserDTO patch);

    User toModel(UpdateUserDTO update);

    @Mapping(target = "image", source = "avatar.path")
    UserProfileDTO toProfile(User user);

    default byte[] map(Image value) {
        if (value == null) return new byte[0];
        return value.getPath();
    }
}