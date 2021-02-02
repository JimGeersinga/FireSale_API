package com.firesale.api.exception;

import com.firesale.api.model.ErrorTypes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorTypes errorType;

    public ResourceNotFoundException(String message, ErrorTypes errorType) {
        super(String.format("Resource was not found: [%s]", message));
        this.errorType = errorType;
    }
}