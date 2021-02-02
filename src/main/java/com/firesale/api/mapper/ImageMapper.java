package com.firesale.api.mapper;

import com.firesale.api.dto.auction.ImageDTO;
import com.firesale.api.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends ModelToDTOMapper<Image, ImageDTO> {
}