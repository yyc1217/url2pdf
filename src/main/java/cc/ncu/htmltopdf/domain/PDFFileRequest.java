package cc.ncu.htmltopdf.domain;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.springframework.util.StringUtils.getFilenameExtension;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cc.ncu.htmltopdf.enums.Orientation;

public class PDFFileRequest {

    private static final String DEFAULT_FILENAME = "result.pdf";

    @NotNull
    private String target;

    @Size(min = 1)
    private String filename;

    private Orientation orientation;

    public PDFFileRequest() {
        this.filename = DEFAULT_FILENAME;
        this.orientation = Orientation.PORTRAIT;
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
        Orientation.valueOf(lowerCase(orientation));
    }

    /**
     * @see http://wkhtmltopdf.org/usage/wkhtmltopdf.txt
     * @return
     */
    public List<String> getCommandArguments() {
        List<String> arguments = new ArrayList<>();
        
        arguments.add("--orientation");
        arguments.add(this.orientation.name());
        arguments.add(this.target);
        
        return arguments;
    }
}
