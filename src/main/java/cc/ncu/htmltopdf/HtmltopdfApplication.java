package cc.ncu.htmltopdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HtmltopdfApplication {

    public static void main(String[] args) throws IOException {
        checkWkHtmlToPdfIsInstalled();
        SpringApplication.run(HtmltopdfApplication.class, args);
    }

    private static void checkWkHtmlToPdfIsInstalled() throws IOException {
        List<String> command = Arrays.asList("wkhtmltopdf", "-V");
        ProcessBuilder pb = new ProcessBuilder(command);

        Process process;

        process = pb.start();
        waitForProcessBeforeContinueCurrentThread(process);

        if (process.exitValue() != 0) {
            throw new RuntimeException("Can not found wkhtmltopdf, see README.md");
        }

    }

    private static void waitForProcessBeforeContinueCurrentThread(Process process) {
        try {
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }    
}
