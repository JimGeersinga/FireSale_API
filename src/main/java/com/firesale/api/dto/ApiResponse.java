package com.firesale.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
}

