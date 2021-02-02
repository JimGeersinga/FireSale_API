package com.firesale.api.dto.auction;

import lombok.Data;

@Data
public class AuctionFilterDTO {

    private long[] categories = new long[0];
    private String[] tags = new String[0];
    private String name;
}
