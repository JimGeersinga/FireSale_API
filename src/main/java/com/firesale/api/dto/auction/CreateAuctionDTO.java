package com.firesale.api.dto.auction;

import com.firesale.api.dto.BaseDTO;
import com.firesale.api.dto.TagDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class CreateAuctionDTO extends BaseDTO {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double minimalBid;
    private Collection<CreateImageDTO> images;
    private Collection<Long> categories;
    private Collection<TagDTO> tags;
}
