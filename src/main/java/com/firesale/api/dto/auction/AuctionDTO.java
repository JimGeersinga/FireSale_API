package com.firesale.api.dto.auction;

import com.firesale.api.dto.BaseDTO;
import com.firesale.api.dto.category.CategoryDTO;
import com.firesale.api.dto.user.UserProfileDTO;
import com.firesale.api.model.AuctionStatus;
import com.firesale.api.dto.bid.BidDTO;
import com.firesale.api.dto.TagDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class AuctionDTO extends BaseDTO {
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private Double minimalBid;
    private Boolean isFeatured;
    private Boolean isFavourite;
    private AuctionStatus status;
    private BidDTO finalBid;
    private UserProfileDTO user;
    private Collection<ImageDTO> images;
    private Collection<CategoryDTO> categories;
    private Collection<TagDTO> tags;
    private Collection<BidDTO> bids;
}
