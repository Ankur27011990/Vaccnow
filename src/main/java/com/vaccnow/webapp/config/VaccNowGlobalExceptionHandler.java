package com.vaccnow.webapp.config;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.vaccnow.webapp.exceptions.ErrorMessage;
import com.vaccnow.webapp.exceptions.UnavailableTimeSlotException;
import com.vaccnow.webapp.exceptions.UnsupportedPaymentMethodException;
import com.vaccnow.webapp.exceptions.UnsupportedTimeSlotException;
import com.vaccnow.webapp.exceptions.UserNotFoundException;
import com.vaccnow.webapp.exceptions.VaccinationCentreNotFound;

@ControllerAdvice(basePackages = "com.vaccnow.webapp.controller")
public class VaccNowGlobalExceptionHandler {

	@ExceptionHandler(value={UnavailableTimeSlotException.class})
	  public ResponseEntity<ErrorMessage> handleUnavailableTimeSlotException(UnavailableTimeSlotException ex, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.CONFLICT.value(),
		        new Date(),
		        "Unavailable timeslots!",
		        request.getDescription(false));
		
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.CONFLICT);
	  }
	
	@ExceptionHandler(value={UnsupportedPaymentMethodException.class})
	  public ResponseEntity<ErrorMessage> handleUnsupportedPaymentMethodException(UnsupportedPaymentMethodException ex, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
		        new Date(),
		        "Invalid Payment Method",
		        request.getDescription(false));
		
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	  }
	
	@ExceptionHandler(value={UnsupportedTimeSlotException.class})
	  public ResponseEntity<ErrorMessage> handleUnsupportedTimeSlotException(UnsupportedTimeSlotException ex, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
		        new Date(),
		        "Unsupported timeslots!",
		        request.getDescription(false));
		
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	  }
	
	@ExceptionHandler(value={UserNotFoundException.class})
	  public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
		        new Date(),
		        "Invalid user provided",
		        request.getDescription(false));
		
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	  }
	
	@ExceptionHandler(value={VaccinationCentreNotFound.class})
	  public ResponseEntity<ErrorMessage> handleVaccinationCentreNotFound(VaccinationCentreNotFound ex, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
		        new Date(),
		        "Invalid VaccinationCentre provided",
		        request.getDescription(false));
		
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	  }
	
	@ExceptionHandler(value={Exception.class})
	  public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
	    ErrorMessage message = new ErrorMessage(
	        HttpStatus.INTERNAL_SERVER_ERROR.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
}
