package com.vezixtor.ontormi.exception;

import org.springframework.http.HttpStatus;

public class OntormiException extends RuntimeException {
	private static final long serialVersionUID = 5292145301605659397L;
	private HttpStatus httpStatus;
	public OntormiException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
