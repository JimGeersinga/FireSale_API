package com.firesale.api.mapper;

import com.firesale.api.dto.auction.AuctionDTO;
import com.firesale.api.dto.auction.CreateAuctionDTO;
import com.firesale.api.dto.user.PersonInfoDTO;
import com.firesale.api.model.Auction;
import com.firesale.api.model.User;
import com.firesale.api.service.AuctionService;
import com.firesale.api.util.SecurityUtil;
import com.firesale.api.dto.auction.AuctionWinningInfoDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {BidMapper.class, UserMapper.class})
public interface AuctionMapper extends ModelToDTOMapper<Auction, AuctionDTO> {
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Auction toModel(CreateAuctionDTO createAuctionDTO);

    @Override
    @Mapping(target = "user", ignore = true)
    Auction toModel(AuctionDTO auction);

    @Mapping(target = "isFavourite", ignore = true)
    AuctionDTO toDTO(Auction auction, @Context AuctionService service);


    @AfterMapping
    default void toDTOAfterMapping(@MappingTarget AuctionDTO target, Auction auction, @Context AuctionService service) {
        if (SecurityUtil.getSecurityContextUser() != null) {
            var userId = SecurityUtil.getSecurityContextUser().getUser().getId();
            boolean isFavourite = service.isFavourite(auction.getId(), userId);
            target.setIsFavourite(isFavourite);
        } else {
            target.setIsFavourite(false);
        }

    }

    default AuctionWinningInfoDTO toWinningInfo(Auction auction) {
        var dto = new AuctionWinningInfoDTO();

        User owner = auction.getUser();
        PersonInfoDTO ownerDto = new PersonInfoDTO();
        ownerDto.setName(owner.getFirstName() + " " + owner.getLastName());
        ownerDto.setEmail(owner.getEmail());
        dto.setOwner(ownerDto);

        User winner = auction.getFinalBid().getUser();
        PersonInfoDTO winnerDto = new PersonInfoDTO();
        winnerDto.setName(winner.getFirstName() + " " + winner.getLastName());
        winnerDto.setEmail(winner.getEmail());

        if (winner.getShippingAddress() != null) {
            AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
            winnerDto.setAddress(addressMapper.toDTO(winner.getAddress()));
        }

        dto.setWinner(winnerDto);

        return dto;
    }
}