package cc.ncu.htmltopdf.service;

import javax.servlet.http.HttpServletResponse;

import cc.ncu.htmltopdf.domain.PDFFileRequest;

public interface IPdfConverter {

	void writePdfToResponse(PDFFileRequest fileRequest, HttpServletResponse response);

}
