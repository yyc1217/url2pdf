package cc.ncu.htmltopdf.service.impl;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.util.Assert.hasText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.ncu.htmltopdf.domain.PDFFileRequest;
import cc.ncu.htmltopdf.service.IPdfConverter;

public class PdfConverter implements IPdfConverter {

	private static final Logger logger = LoggerFactory.getLogger(PdfConverter.class);

	@Override
	public void writePdfToResponse(PDFFileRequest fileRequest, HttpServletResponse response) {
		String pdfFileName = fileRequest.getFilename();
		requireNonNull(pdfFileName, "The file name of the created PDF must be set");
		hasText(pdfFileName, "File name of the created PDF cannot be empty or be blank");

		String targetUrl = fileRequest.getTarget();
		requireNonNull(targetUrl, "Target HTML url must be set");
		hasText(targetUrl, "Target HTML url cannot be empty");

		List<String> pdfCommand = Arrays.asList("wkhtmltopdf", targetUrl, "-");

		ProcessBuilder pb = new ProcessBuilder(pdfCommand);
		Process pdfProcess;

		try {
			pdfProcess = pb.start();

			try (InputStream in = pdfProcess.getInputStream()) {
				writeCreatedPdfFileToResponse(in, response);
				waitForProcessBeforeContinueCurrentThread(pdfProcess);
				requireSuccessfulExitStatus(pdfProcess);
				setResponseHeaders(response, fileRequest);
			} catch (Exception ex) {
				writeErrorMessageToLog(ex, pdfProcess);
				throw new RuntimeException("PDF generation failed");
			} finally {
				pdfProcess.destroy();
			}
		} catch (IOException ex) {
			throw new RuntimeException("PDF generation failed");
		}
	}

	private void writeCreatedPdfFileToResponse(InputStream in, HttpServletResponse response) throws IOException {
		OutputStream out = response.getOutputStream();
		IOUtils.copy(in, out);
		out.flush();
	}

	private void waitForProcessBeforeContinueCurrentThread(Process process) {
		try {
			process.waitFor(5, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	private void requireSuccessfulExitStatus(Process process) {
		if (process.exitValue() != 0) {
			throw new RuntimeException("PDF generation failed");
		}
	}

	private void setResponseHeaders(HttpServletResponse response, PDFFileRequest fileRequest) {
		response.setContentType(APPLICATION_JSON_UTF8.getType());
		response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + fileRequest.getFilename() + "\"");
	}

	private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
		logger.error("Could not create PDF because an exception was thrown: ", ex);
		logger.error("The exit value of PDF process is: {}", pdfProcess.exitValue());

		String errorMessage = IOUtils.toString(pdfProcess.getErrorStream());
		logger.error("PDF process ended with error message: {}", errorMessage);
	}
}
