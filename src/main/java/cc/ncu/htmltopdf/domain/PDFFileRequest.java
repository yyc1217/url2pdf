package cc.ncu.htmltopdf.domain;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.springframework.util.StringUtils.getFilenameExtension;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import cc.ncu.htmltopdf.enums.Orientation;
import cc.ncu.htmltopdf.enums.PageSize;

public class PDFFileRequest {

    private static final String DEFAULT_FILENAME = "result.pdf";

    private static final Orientation DEFAULT_ORIENTATION = Orientation.PORTRAIT;
    
    private static final PageSize DEFAULT_PAGESIZE = PageSize.A4;
    
    private static final String DEFAULT_VIEWPORT = "1280X1024";
    
    @NotNull
    private String target;

    @Size(min = 1)
    private String filename;

    private Orientation orientation;
    
    private PageSize pageSize;

    @Pattern(regexp = "\\d+x\\d+")
    private String viewport;
    
    public PDFFileRequest() {
        this.filename = DEFAULT_FILENAME;
        this.orientation = DEFAULT_ORIENTATION;
        this.pageSize = DEFAULT_PAGESIZE;
        this.viewport = DEFAULT_VIEWPORT;
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

        if (!hasText(filename)) {
            this.filename = DEFAULT_FILENAME;
        }

        String extension = getFilenameExtension(this.filename);
        if (!"pdf".equalsIgnoreCase(extension)) {
            this.filename += ".pdf";
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setOrientation(String orientation) {
        Orientation.valueOf(upperCase(orientation));
    }

    public PageSize getPageSize() {
        return this.pageSize;
    }
    
    public void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }
    
    public void setPageSize(String pageSize) {
        PageSize.valueOf(upperCase(pageSize));
    }
    
    public void setViewport(String viewport) {
        this.viewport = viewport;
    }
    
    public String getViewport() {
        return this.viewport;
    }
    
    /**
     * @see http://wkhtmltopdf.org/usage/wkhtmltopdf.txt
     * @return
     */
    public List<String> getCommandArguments() {
        List<String> arguments = new ArrayList<>();
        
        arguments.add("--orientation");
        arguments.add(this.orientation.name());
        
        arguments.add("--page-size");
        arguments.add(this.pageSize.name());
        
        arguments.add("--viewport-size");
        arguments.add(this.viewport);
        
        arguments.add(this.target);
        
        return arguments;
    }
}
