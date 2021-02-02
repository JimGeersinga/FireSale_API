package com.firesale.api.dto.auction;

import com.firesale.api.dto.user.PersonInfoDTO;
import lombok.Data;

@Data
public class AuctionWinningInfoDTO {
    private PersonInfoDTO owner;

    private PersonInfoDTO winner;
}
