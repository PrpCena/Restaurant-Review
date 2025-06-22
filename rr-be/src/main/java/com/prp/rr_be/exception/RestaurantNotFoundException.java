package com.prp.rr_be.exception;

public class RestaurantNotFoundException extends BaseException {
  
  public RestaurantNotFoundException(Throwable cause) {
	super(cause);
  }
  
  public RestaurantNotFoundException(String message, Throwable cause) {
	super(message, cause);
  }
  
  public RestaurantNotFoundException(String message) {
	super(message);
  }
  
  public RestaurantNotFoundException() {
  }
}
