package com.firesale.api.dto;

import com.firesale.api.model.ErrorTypes;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse extends ApiResponse<Void> {
    private final String errorCode;
    private final String errorMessage;
    private final List<String> errorDetails;

    public ErrorResponse(ErrorTypes error, String message) {
        this(error, message, null);
    }

    public ErrorResponse(ErrorTypes error, String message, List<String> details) {
        super(false, null);

        this.errorCode = error.getCode();
        this.errorMessage = message;
        this.errorDetails = details;
    }
}
