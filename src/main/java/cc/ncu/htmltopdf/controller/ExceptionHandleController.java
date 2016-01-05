package cc.ncu.htmltopdf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cc.ncu.htmltopdf.domain.ErrorMessages;
import cc.ncu.htmltopdf.exception.BadParameterException;

@ControllerAdvice
public class ExceptionHandleController {

	@ExceptionHandler(value = BadParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorMessages exception(BadParameterException exception) {
		return new ErrorMessages(exception);
	}
}
