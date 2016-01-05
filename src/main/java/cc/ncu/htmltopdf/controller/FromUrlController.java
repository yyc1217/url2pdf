package cc.ncu.htmltopdf.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.ncu.htmltopdf.domain.PDFFileRequest;
import cc.ncu.htmltopdf.exception.BadParameterException;
import cc.ncu.htmltopdf.service.IPdfConverter;

@RestController
public class FromUrlController {

	@Autowired
	private IPdfConverter pdfConverter;

	@RequestMapping(value = "/fromUrl", method = RequestMethod.GET)
	void createPdf(HttpServletResponse response, @Valid @ModelAttribute PDFFileRequest fileRequest, BindingResult result) {
		
		if (result.hasErrors()) {
			throw new BadParameterException(result);
		}
		
		this.pdfConverter.writePdfToResponse(fileRequest, response);
	}
}
