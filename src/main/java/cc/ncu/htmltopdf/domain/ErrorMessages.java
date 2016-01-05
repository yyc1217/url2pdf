package cc.ncu.htmltopdf.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import cc.ncu.htmltopdf.exception.BadParameterException;

public class ErrorMessages {

    private List<ErrorMessage> errorMessages;

    public ErrorMessages() {
        errorMessages = new ArrayList<>();
    }

    public ErrorMessages(BadParameterException exception) {
        BindingResult result = exception.getResult();
        errorMessages = result.getFieldErrors().stream().map(ErrorMessage::new).collect(toList());
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public class ErrorMessage {

        private String parameter;

        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(FieldError error) {
            this.parameter = error.getField();
            this.message = error.getDefaultMessage();
        }

        public String getParameter() {
            return parameter;
        }

        public String getMessage() {
            return message;
        }

    }

}
