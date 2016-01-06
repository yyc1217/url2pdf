package cc.ncu.htmltopdf;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;


@SpringBootApplication
public class HtmltopdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(HtmltopdfApplication.class, args);
	}
	
    @Bean(name = "localeResolver")
    public CookieLocaleResolver localeResolver() {
            CookieLocaleResolver localeResolver = new CookieLocaleResolver();
            localeResolver.setDefaultLocale(Locale.ENGLISH);
            return localeResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:i18n/messages");
            messageSource.setCacheSeconds(10); //reload messages every 10 seconds
            return messageSource;
    }
}
