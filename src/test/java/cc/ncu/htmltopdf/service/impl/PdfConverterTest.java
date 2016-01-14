package cc.ncu.htmltopdf.service.impl;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.ncu.htmltopdf.domain.PDFFileRequest;

public class PdfConverterTest {

    PdfConverter converter;

    @Before
    public void setUp() {
        converter = new PdfConverter();
    }

    @Test
    public void testBuildCommand() {

        PDFFileRequest request = new PDFFileRequest();
        request.setTarget("fakeTarget");
        List<String> command = converter.buildCommand(request);
        
        assertThat(command, hasItem("wkhtmltopdf"));
        assertThat(command, hasItem("fakeTarget"));
        assertThat(command, hasItem("-"));
    }

}
