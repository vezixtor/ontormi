package com.vezixtor.ontormi.model.dto;

import com.vezixtor.ontormi.exception.OntormiException;

public class ErrorResponse {
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(OntormiException exception) {
        this.status = exception.getHttpStatus().value();
        this.error = exception.getHttpStatus().getReasonPhrase();
        this.message = exception.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
