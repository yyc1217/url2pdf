package cc.ncu.htmltopdf.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

public class PDFFileRequest {
	
	@NotNull
	private String target;
	
	@Size(min=1)
	private String filename;

	public PDFFileRequest() {
		this.filename = "converted.pdf";
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
		
		String extension = StringUtils.getFilenameExtension(filename);
		if (!"pdf".equalsIgnoreCase(extension)) {
			this.filename += ".pdf";
		}
	}
}
