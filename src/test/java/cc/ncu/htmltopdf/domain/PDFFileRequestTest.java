package cc.ncu.htmltopdf.domain;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class PDFFileRequestTest {

	private static Validator validator;

	@Before
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	public void targetUrlShouldNotNull() {
        assertTrue(isViolate(new PDFFileRequest(), "target"));
	}
	
	@Test
	public void extensionShouldAlwaysExists() {
		PDFFileRequest request = new PDFFileRequest();
		request.setFilename("test");
		
		assertTrue(StringUtils.endsWithIgnoreCase(request.getFilename(), ".pdf"));
	}

	@Test
	public void viewportShouldMatchPattern() {
	    PDFFileRequest request = new PDFFileRequest();
	    request.setViewport("x");
	    
	    assertTrue(isViolate(request, "viewport"));
	    
	    request.setViewport("1024x768 | sudo rm -rf /");
	    assertTrue(isViolate(request, "viewport"));
	}
	
	private Boolean isViolate(PDFFileRequest request, String property) {
	    Set<ConstraintViolation<PDFFileRequest>> violations = validator.validate(request);
	    return violations.stream().filter(constr -> {
            return property.equalsIgnoreCase(constr.getPropertyPath().toString());
        }).findAny().isPresent();
	}
	
}
