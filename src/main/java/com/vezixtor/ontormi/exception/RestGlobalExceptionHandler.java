package com.vezixtor.ontormi.exception;

import com.vezixtor.ontormi.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<FieldError> errors = ex.getBindingResult().getFieldErrors();
		
		StringBuilder message = new StringBuilder();
		for (FieldError error: errors) {
			message.append(error.getField()).append(", ").append(error.getDefaultMessage()).append("\n");
		}
		
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message.toString());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OntormiException.class)
	public ResponseEntity<?> handleOntormiException(OntormiException exception) {
		return new ResponseEntity<>(new ErrorResponse(exception), exception.getHttpStatus());
	}

}