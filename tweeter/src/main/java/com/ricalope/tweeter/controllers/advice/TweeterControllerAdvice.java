package com.ricalope.tweeter.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ricalope.tweeter.exceptions.BadRequestException;
import com.ricalope.tweeter.exceptions.NotAuthorizedException;
import com.ricalope.tweeter.exceptions.NotFoundException;
import com.ricalope.tweeter.model.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;

@ResponseBody
@ControllerAdvice(basePackages = "com.ricalope.tweeter.controllers")
public class TweeterControllerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException){
        return new ErrorDto(badRequestException.getMessage());
    }
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException){
        return new ErrorDto(notFoundException.getMessage());
    }
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDto handleUnauthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException){
        return new ErrorDto(notAuthorizedException.getMessage());
    }

}
