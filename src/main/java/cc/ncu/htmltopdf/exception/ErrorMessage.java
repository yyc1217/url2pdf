package cc.ncu.htmltopdf.exception;

import org.springframework.validation.FieldError;

public class ErrorMessage {

    private String parameter;

    private String message;

    public ErrorMessage() {
    }
    
    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(FieldError error) {
        this.parameter = error.getField();
    }

    public String getParameter() {
        return parameter;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}