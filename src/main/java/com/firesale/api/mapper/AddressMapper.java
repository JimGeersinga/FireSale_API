package com.firesale.api.mapper;

import com.firesale.api.dto.address.AddressDTO;
import com.firesale.api.dto.address.PatchAddressDTO;
import com.firesale.api.dto.address.UpdateAddressDTO;
import com.firesale.api.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper extends ModelToDTOMapper<Address, AddressDTO> {
    Address toModel(PatchAddressDTO patch);

    Address toModel(UpdateAddressDTO update);
}