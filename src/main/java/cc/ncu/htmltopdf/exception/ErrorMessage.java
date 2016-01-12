package cc.ncu.htmltopdf.exception;

import java.time.LocalDateTime;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ErrorMessage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parameter;

    private String message;
    
    private LocalDateTime timestamp;

    public ErrorMessage() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorMessage(String message) {
        this();
        this.message = message;
    }

    public ErrorMessage(String parameter, String message) {
        this(message);
        this.parameter = parameter;
    }
    
    public ErrorMessage(FieldError error) {
        this();
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

    public String getTimestamp() {
        return timestamp.toString();
    }

}