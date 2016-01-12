package cc.ncu.htmltopdf.controller;

import static cc.ncu.htmltopdf.config.RequestMappingPath.url2pdf;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.ncu.htmltopdf.domain.PDFFileRequest;
import cc.ncu.htmltopdf.exception.BadParameterException;
import cc.ncu.htmltopdf.service.IPdfConverter;

@RestController
public class Url2PdfController {

    private static final Logger logger = LoggerFactory.getLogger(Url2PdfController.class);
    
	@Autowired
	private IPdfConverter pdfConverter;

	@RequestMapping(value = url2pdf, method = RequestMethod.GET)
	void createPdf(HttpServletResponse response, @RequestHeader HttpHeaders httpHeader, @Valid @ModelAttribute PDFFileRequest fileRequest, BindingResult result) {
		
	    logger.info("Access: [url={}], [header={}]", url2pdf, httpHeader);
	    
		if (result.hasErrors()) {
			throw new BadParameterException(result);
		}
		
		this.pdfConverter.writePdfToResponse(fileRequest, response);
	}
}
