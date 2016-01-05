package cc.ncu.htmltopdf.exception;

import org.springframework.validation.BindingResult;

public class BadParameterException extends IllegalArgumentException {

    private static final long serialVersionUID = -3160349682314301127L;

    private BindingResult result;
    
    public BadParameterException() {
        super();
    }
    
    public BadParameterException(BindingResult result) {
        this.result = result;
    }

    public BindingResult getResult() {
        return result;
    }
}
