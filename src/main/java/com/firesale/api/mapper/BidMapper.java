package com.firesale.api.mapper;

import com.firesale.api.model.Bid;
import com.firesale.api.dto.bid.BidDTO;
import com.firesale.api.dto.bid.CreateBidDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BidMapper extends ModelToDTOMapper<Bid, BidDTO> {
    Bid toModel(BidDTO dto);

    Bid toModel(CreateBidDTO dto);

    @Override
    @Mapping(target = "userName", source = "user.displayName")
    @Mapping(target = "userId", source = "user.id")
    BidDTO toDTO(Bid model);
}