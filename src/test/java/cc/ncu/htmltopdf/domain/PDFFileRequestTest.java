package cc.ncu.htmltopdf.domain;

import static org.junit.Assert.*;

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
		Set<ConstraintViolation<PDFFileRequest>> violations = validator.validate(new PDFFileRequest());
		ConstraintViolation<PDFFileRequest> violation = violations.iterator().next();
		
		assertEquals(1, violations.size());
		assertEquals("target", violation.getPropertyPath().toString());
	}
	
	@Test
	public void shouldAlwaysHasExtension() {
		PDFFileRequest request = new PDFFileRequest();
		request.setFilename("test");
		
		assertTrue(StringUtils.endsWithIgnoreCase(request.getFilename(), ".pdf"));
	}

}
