package com.firesale.api.dto.bid;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CreateBidDTO {
    @Min(1)
    @NotNull
    private Double value;
}
