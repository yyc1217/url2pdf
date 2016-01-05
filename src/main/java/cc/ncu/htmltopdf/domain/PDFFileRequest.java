package cc.ncu.htmltopdf.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

public class PDFFileRequest {
	
    private static final String DEFAULT_FILENAME = "result.pdf";
    
	@NotNull
	private String target;
	
	@Size(min=1)
	private String filename;

	public PDFFileRequest() {
		this.filename = DEFAULT_FILENAME;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		
		this.filename = filename;
		
		if (!StringUtils.hasText(filename)) {
		    this.filename = DEFAULT_FILENAME;
		}
		
		String extension = StringUtils.getFilenameExtension(this.filename);
		if (!"pdf".equalsIgnoreCase(extension)) {
			this.filename += ".pdf";
		}
	}
}
