package cc.ncu.htmltopdf.service.impl;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.net.MediaType;

import cc.ncu.htmltopdf.domain.PDFFileRequest;
import cc.ncu.htmltopdf.service.IPdfConverter;

@Service
public class PdfConverter implements IPdfConverter {

	private static final Logger logger = LoggerFactory.getLogger(PdfConverter.class);
	
	private static final String UTF_8 = "UTF-8";
	
	private static final String ISO_8859_1 = "ISO-8859-1";
	
	@Value("${pdf.creation.wait.seconds}")
	private Integer waitSeconds;

	@Override
	public void writePdfToResponse(PDFFileRequest fileRequest, HttpServletResponse response) {

		List<String> command = buildCommand(fileRequest);
		
		ProcessBuilder pb = new ProcessBuilder(command);
		Process pdfProcess;

		try {
			pdfProcess = pb.start();

			try (InputStream in = pdfProcess.getInputStream()) {
				setResponseHeaders(response, fileRequest);
				writeCreatedPdfFileToResponse(in, response);
				waitForProcessBeforeContinueCurrentThread(pdfProcess);
				requireSuccessfulExitStatus(pdfProcess);
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

	List<String> buildCommand(PDFFileRequest fileRequest) {
        List<String> arguments = fileRequest.getCommandArguments();
        List<String> command = new ArrayList<>();
        
        command.add("wkhtmltopdf");
        command.addAll(arguments);
        command.add("-");
        return command;
    }

    private void writeCreatedPdfFileToResponse(InputStream in, HttpServletResponse response) throws IOException {
		OutputStream out = response.getOutputStream();
		IOUtils.copy(in, out);
	}

	private void waitForProcessBeforeContinueCurrentThread(Process process) {
		try {
			process.waitFor(waitSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	private void requireSuccessfulExitStatus(Process process) {
		if (process.exitValue() != 0) {
			throw new RuntimeException("PDF generation failed");
		}
	}

	private void setResponseHeaders(HttpServletResponse response, PDFFileRequest fileRequest) throws UnsupportedEncodingException {
		response.setContentType(MediaType.PDF.withParameter("charset", UTF_8).toString());
		response.setCharacterEncoding(UTF_8);
		
		String filename = new String(fileRequest.getFilename().getBytes(UTF_8), ISO_8859_1);
		response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + filename);
	}

	private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
		logger.error("Could not create PDF because an exception was thrown: ", ex);
		logger.error("The exit value of PDF process is: {}", pdfProcess.exitValue());

		String errorMessage = IOUtils.toString(pdfProcess.getErrorStream());
		logger.error("PDF process ended with error message: {}", errorMessage);
	}
}
