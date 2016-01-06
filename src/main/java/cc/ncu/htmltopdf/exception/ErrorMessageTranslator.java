package cc.ncu.htmltopdf.exception;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorMessageTranslator {

    @Autowired
    private MessageSource messageSource;
    
    private Function<FieldError, ErrorMessage> toErrorMessage = error -> {
        ErrorMessage errorMessage = new ErrorMessage(error);
        String message = messageSource.getMessage(error, null);
        errorMessage.setMessage(message);
        return errorMessage;
    };
    
    public List<ErrorMessage> translate(BadParameterException exception) {
        BindingResult result = exception.getResult();
        return result.getFieldErrors().stream().map(toErrorMessage).collect(toList());
    }

}
