package com.prp.rr_be.controllers;


import com.prp.rr_be.domain.dtos.ErrorDto;
import com.prp.rr_be.exception.BaseException;
import com.prp.rr_be.exception.RestaurantNotFoundException;
import com.prp.rr_be.exception.ReviewNotAllowedException;
import com.prp.rr_be.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {
  
  @ExceptionHandler(ReviewNotAllowedException.class)
  public ResponseEntity<ErrorDto> handleRestaurantReviewNotAllowedException(
	ReviewNotAllowedException ex) {
	log.error("Caught ReviewNotAllowedException exception", ex);
	ErrorDto error = ErrorDto
					   .builder()
					   .status(HttpStatus.BAD_REQUEST.value())
					   .message("The specified review cannot be created or updated")
					   .build();
	return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(RestaurantNotFoundException.class)
  public ResponseEntity<ErrorDto> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
	log.error("Caught RestaurantNotFoundException", ex);
	ErrorDto errorDto = ErrorDto
						  .builder()
						  .status(HttpStatus.NOT_FOUND.value())
						  .message("The specified restaurant wasn't found")
						  .build();
	return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
  }
  
  
  @ExceptionHandler(StorageException.class)
  public ResponseEntity<ErrorDto> handleStorageExcepion(StorageException e) {
	log.error("Storage exception caught", e);
	
	ErrorDto errorDto = ErrorDto
						  .builder()
						  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
						  .message("unable to save or retrieve resources")
						  .build();
	
	return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  // Handle our base application exception
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorDto> handleBaseException(BaseException ex) {
	log.error("Caught BaseException", ex);
	ErrorDto error = ErrorDto
					   .builder()
					   .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					   .message("An unexpected error occurred")
					   .build();
	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  // Catch-all for unexpected exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
	log.error("Caught unexpected exception", ex);
	ErrorDto error = ErrorDto
					   .builder()
					   .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					   .message("An unexpected error occurred")
					   .build();
	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
	log.error("Validation error", ex);
	// Collect all validation errors into a single string
	String errorMessage = ex
							.getBindingResult()
							.getFieldErrors()
							.stream()
							.map(error -> error.getField() + ": " + error.getDefaultMessage())
							.collect(Collectors.joining(", "));
	ErrorDto errorDto = ErrorDto
						  .builder()
						  .status(HttpStatus.BAD_REQUEST.value())
						  .message("Validation failed: " + errorMessage)
						  .build();
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
}
