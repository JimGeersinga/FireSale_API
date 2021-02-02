package com.firesale.api.dto.auction;

import lombok.Data;

@Data
public class CreateImageDTO {
    private Long id;
    private byte[] path;
    private String type;
    private Integer sort;

}
