package cc.ncu.htmltopdf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cc.ncu.htmltopdf.exception.BadParameterException;
import cc.ncu.htmltopdf.exception.ErrorMessage;
import cc.ncu.htmltopdf.exception.ErrorMessageTranslator;

@ControllerAdvice
public class ExceptionHandleController {

    @Autowired
    private ErrorMessageTranslator errorMessageTranslator; 
    
	@ExceptionHandler(value = BadParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<ErrorMessage> badParameterException(BadParameterException exception) {
		return errorMessageTranslator.translate(exception);
	}
	
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorMessage exception(Exception exception) {
	    return new ErrorMessage("Internal Server Error.");
	}
	
}
