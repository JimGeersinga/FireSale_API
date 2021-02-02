package com.firesale.api.dto.auction;

import com.firesale.api.dto.BaseDTO;
import lombok.Data;

@Data
public class ImageDTO extends BaseDTO {
    private byte[] path;
    private String type;
    private Integer sort;
}
