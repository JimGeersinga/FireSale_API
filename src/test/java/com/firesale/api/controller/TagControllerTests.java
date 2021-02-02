package com.firesale.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firesale.api.dto.TagDTO;
import com.firesale.api.exception.GlobalExceptionHandler;
import com.firesale.api.mapper.TagMapper;
import com.firesale.api.model.Tag;
import com.firesale.api.service.TagService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class TagControllerTests {
    private MockMvc mvc;
    @Mock
    private TagService tagService;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(tagController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAll() throws Exception {
        // given
        Tag t = new Tag();
        t.setName("test");
        t.setId(1L);

        TagDTO dto = new TagDTO();
        dto.setName("test");
        dto.setId(1L);
        doReturn(Collections.singletonList(t)).when(tagService).getAllTags();
        doReturn(dto).when(tagMapper).toDTO(any(Tag.class));


        // when
        MockHttpServletResponse response = mvc.perform(
                get("/tags")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");
    }

    @Test
    void getSearch() throws Exception {
        // given
        Tag t = new Tag();
        t.setName("test");
        t.setId(1L);

        TagDTO dto = new TagDTO();
        dto.setName("test");
        dto.setId(1L);
        doReturn(Collections.singletonList(t)).when(tagService).searchTagsByName(anyString());
        doReturn(dto).when(tagMapper).toDTO(any(Tag.class));


        // when
        MockHttpServletResponse response = mvc.perform(
                get("/tags?searchterm=test")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");
    }
}
