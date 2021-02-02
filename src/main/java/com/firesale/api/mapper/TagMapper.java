package com.firesale.api.mapper;

import com.firesale.api.model.Tag;
import com.firesale.api.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends ModelToDTOMapper<Tag, TagDTO> {
    Tag toModel(TagDTO tagDto);
}
