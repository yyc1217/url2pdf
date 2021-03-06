package cc.ncu.htmltopdf.exception;

import org.springframework.validation.BindingResult;

public class BadParameterException extends IllegalArgumentException {

    private static final long serialVersionUID = -3160349682314301127L;

    private BindingResult result;
    
    public BadParameterException() {
        super();
    }
    
    public BadParameterException(BindingResult result) {
        super(result.getFieldErrors().toString());
        this.result = result;
    }

    public BadParameterException(String forbiddenMessage) {
        super(forbiddenMessage);
    }

    public BindingResult getResult() {
        return result;
    }
}
