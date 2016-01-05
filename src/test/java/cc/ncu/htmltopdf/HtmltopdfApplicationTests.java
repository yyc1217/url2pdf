package cc.ncu.htmltopdf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import cc.ncu.htmltopdf.HtmltopdfApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HtmltopdfApplication.class)
@WebAppConfiguration
public class HtmltopdfApplicationTests {

	@Test
	public void contextLoads() {
	}

}
