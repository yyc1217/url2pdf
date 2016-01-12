package cc.ncu.htmltopdf.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import cc.ncu.htmltopdf.exception.BadParameterException;
import cc.ncu.htmltopdf.exception.ErrorMessage;
import cc.ncu.htmltopdf.exception.ErrorMessageTranslator;

@RestController
@ControllerAdvice
public class ExceptionHandleController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);
    
    private static final String PATH = "/error";
    
    @Autowired
    private ErrorMessageTranslator errorMessageTranslator; 

    @Autowired
    private ErrorAttributes errorAttributes;
    
    @RequestMapping(value = PATH)
    public ErrorMessage error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        logger.error("error : {} ", errorAttributes);
        String message = (String) errorAttributes.get("message");
        return new ErrorMessage(message);
    }
    
    @Override
    public String getErrorPath() {
        return PATH;
    }
    
    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, false);
    }
    
	@ExceptionHandler(value = BadParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public List<ErrorMessage> badParameterException(BadParameterException exception) {
	    logger.error("Bad Parameter: {}", exception.getMessage());
		return errorMessageTranslator.translate(exception);
	}
	
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage exception(Exception exception) {
	    logger.error("Internal Server Error: {}", exception.getMessage());
	    return new ErrorMessage("Internal Server Error.");
	}

}
