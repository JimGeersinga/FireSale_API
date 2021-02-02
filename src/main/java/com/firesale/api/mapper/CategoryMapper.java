package com.firesale.api.mapper;

import com.firesale.api.dto.category.CategoryDTO;
import com.firesale.api.dto.category.UpsertCategoryDTO;
import com.firesale.api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends ModelToDTOMapper<Category, CategoryDTO> {

    Category toModel(UpsertCategoryDTO upsertCategoryDTO);
}
