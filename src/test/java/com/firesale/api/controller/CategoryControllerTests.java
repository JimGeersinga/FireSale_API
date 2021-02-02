package com.firesale.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firesale.api.dto.category.CategoryDTO;
import com.firesale.api.dto.category.UpsertCategoryDTO;
import com.firesale.api.exception.GlobalExceptionHandler;
import com.firesale.api.mapper.CategoryMapper;
import com.firesale.api.model.Category;
import com.firesale.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTests {
    private MockMvc mvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private JacksonTester<UpsertCategoryDTO> upsertCategoryDTOJacksonTester;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void canRetrieveByIdWhenExists() throws Exception {
        // given
        Category c = new Category();
        c.setName("test");
        c.setId(1L);
        when(categoryMapper.toDTO(any(Category.class))).thenAnswer((i)->{
            var category = (Category)i.getArguments()[0];
            var dto = new CategoryDTO();
            dto.setName(category.getName());
            return dto;
        });
        given(categoryService.getAvailableCategories())
                .willReturn(Collections.singletonList(c));
        // when
        MockHttpServletResponse response = mvc.perform(
                 get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");
    }

    @Test
    void postDTO() throws Exception {
        // given
        Category c = new Category();
        c.setName("test");
        c.setId(1L);
        when(categoryMapper.toDTO(any(Category.class))).thenAnswer((i)->{
            var category = (Category)i.getArguments()[0];
            var dto = new CategoryDTO();
            dto.setName(category.getName());
            return dto;
        });
        when(categoryMapper.toModel(any(UpsertCategoryDTO.class))).thenAnswer((i)->{
            var category = (UpsertCategoryDTO)i.getArguments()[0];
            var dto = new Category();
            dto.setName(category.getName());
            return dto;
        });

        UpsertCategoryDTO dto = new UpsertCategoryDTO();
        dto.setName("test");

        when(categoryService.create(any(Category.class))).thenAnswer((i)-> i.getArguments()[0]);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(upsertCategoryDTOJacksonTester.write(dto).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains("test");
    }

    @Test
    void putDTO() throws Exception {
        // given
        Category c = new Category();
        c.setName("test");
        c.setId(1L);
        when(categoryMapper.toModel(any(UpsertCategoryDTO.class))).thenAnswer((i)->{
            var category = (UpsertCategoryDTO)i.getArguments()[0];
            var dto = new Category();
            dto.setName(category.getName());
            return dto;
        });

        UpsertCategoryDTO dto = new UpsertCategoryDTO();
        dto.setName("test");

        when(categoryService.updateCategory(anyLong(),any(Category.class))).thenAnswer((i)-> i.getArguments()[1]);

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/categories/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(upsertCategoryDTOJacksonTester.write(dto).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @Test
    void deleteAction() throws Exception {
        // given
        doNothing().when(categoryService).deleteCategory(anyLong());

        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }




    @Test
    void getArchived() throws Exception {
        // given
        Category c = new Category();
        c.setName("test");
        c.setId(1L);
        when(categoryMapper.toDTO(any(Category.class))).thenAnswer((i)->{
            var category = (Category)i.getArguments()[0];
            var dto = new CategoryDTO();
            dto.setName(category.getName());
            return dto;
        });
        given(categoryService.getArchivedCategories())
                .willReturn(Collections.singletonList(c));
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/categories/archived")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");
    }
}
