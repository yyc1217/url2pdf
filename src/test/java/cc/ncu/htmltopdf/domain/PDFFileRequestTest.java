package cc.ncu.htmltopdf.domain;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import cc.ncu.htmltopdf.enums.Orientation;
import cc.ncu.htmltopdf.enums.PageSize;

public class PDFFileRequestTest {

	private static Validator validator;

	private PDFFileRequest request;
	
	@Before
	public void setUp() {
	    request = new PDFFileRequest();
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	public void targetUrlShouldNotNull() {
        assertTrue(isViolate(new PDFFileRequest(), "target"));
	}
	
	@Test
	public void extensionShouldAlwaysExists() {
		request.setFilename("test");
		
		assertTrue(StringUtils.endsWithIgnoreCase(request.getFilename(), ".pdf"));
	}

	@Test
	public void viewportShouldMatchPattern() {
	    request.setViewport("x");
	    
	    assertTrue(isViolate(request, "viewport"));
	    
	    request.setViewport("1024x768 | sudo rm -rf /");
	    assertTrue(isViolate(request, "viewport"));
	}
	
	@Test
	public void shouldExistdefaultFilename() {
	    request.setFilename(null);
	    
	    assertEquals(PDFFileRequest.DEFAULT_FILENAME, request.getFilename());
	}
	
	@Test
	public void orientationShouldAcceptNonUppercase() {
	    request.setOrientation("landscape");
	    
	    assertEquals(Orientation.LANDSCAPE, request.getOrientation());
	}
	
    @Test
    public void pagesizeShouldAcceptNonUppercase() {
        request.setPageSize("a4");
        
        assertEquals(PageSize.A4, request.getPageSize());
    }
    
    @Test
    public void testArgumentCommands() {
        
        Orientation testOrientation = Orientation.LANDSCAPE;
        PageSize testPageSize = PageSize.B3;
        String testViewport = "1999x2888";
        String testTarget = "http://test.com";
        
        request.setOrientation(testOrientation);
        request.setPageSize(testPageSize);
        request.setTarget(testTarget);
        request.setViewport(testViewport);
        
        List<String> arguments = request.getCommandArguments();
        
        assertThat(arguments, hasItem("--orientation"));
        assertThat(arguments, hasItem(testOrientation.name()));
        
        assertThat(arguments, hasItem("--page-size"));
        assertThat(arguments, hasItem(testPageSize.name()));
        
        assertThat(arguments, hasItem("--viewport-size"));
        assertThat(arguments, hasItem(testViewport));
        
        assertThat(arguments, hasItem(testTarget));
    }
	
	private Boolean isViolate(PDFFileRequest request, String property) {
	    Set<ConstraintViolation<PDFFileRequest>> violations = validator.validate(request);
	    return violations.stream().filter(constr -> {
            return property.equalsIgnoreCase(constr.getPropertyPath().toString());
        }).findAny().isPresent();
	}
	
}
