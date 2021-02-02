package com.firesale.api.dto.auction;

import lombok.Data;

@Data
public class AuctionJoinRequestDTO {
    private int userId;
    private int auctionId;
}
