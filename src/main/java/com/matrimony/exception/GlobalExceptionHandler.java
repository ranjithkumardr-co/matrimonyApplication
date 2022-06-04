package com.matrimony.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleException(MethodArgumentNotValidException ex) {
		List<FieldError> errors = ex.getFieldErrors();
		var validationErrorResponse = new ValidationErrorResponse();
		validationErrorResponse.setDateTime(LocalDateTime.now());
		validationErrorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		validationErrorResponse.setMessage("Input data has some errors... Please provide proper data");

		for (FieldError f : errors) {
			validationErrorResponse.getErrors().put(f.getField(), f.getDefaultMessage());
		}

		return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorResponse> handleException(ConstraintViolationException ex) {
		var validationErrorResponse = new ValidationErrorResponse();
		validationErrorResponse.setDateTime(LocalDateTime.now());
		validationErrorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		validationErrorResponse.setMessage("Input data has some errors... Please provide proper data");

		ex.getConstraintViolations()
				.forEach(error -> validationErrorResponse.getErrors().put("field", error.getMessage()));

		return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleException(NoSuchElementException ex) {
		var errorResponse = new ErrorResponse();
		errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setDateTime(LocalDateTime.now());
		errorResponse.setMessage("you can't accept the request because you are not liked by them");

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleException(IdNotFoundException ex) {

		var errorResponse = new ErrorResponse();
		errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setDateTime(LocalDateTime.now());
		errorResponse.setMessage(ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleException(DataNotFoundException ex) {


		var errorResponse = new ErrorResponse();
		errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setDateTime(LocalDateTime.now());
		errorResponse.setMessage(ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(WrongCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleException(WrongCredentialsException ex) {


		var errorResponse = new ErrorResponse();
		errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setDateTime(LocalDateTime.now());
		errorResponse.setMessage(ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(ArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleException(ArgumentNotValidException ex) {

		var errorResponse = new ErrorResponse();
		errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setDateTime(LocalDateTime.now());
		errorResponse.setMessage(ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
