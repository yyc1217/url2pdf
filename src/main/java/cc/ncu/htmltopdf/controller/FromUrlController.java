package cc.ncu.htmltopdf.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.ncu.htmltopdf.domain.PDFFileRequest;
import cc.ncu.htmltopdf.service.IPdfConverter;

@RestController
public class FromUrlController {

	@Autowired
	private IPdfConverter pdfConverter;

	@RequestMapping(value = "/fromUrl", method = RequestMethod.GET)
	void createPdf(HttpServletResponse response, @RequestParam PDFFileRequest fileRequest) {
		pdfConverter.writePdfToResponse(fileRequest, response);
	}
}
