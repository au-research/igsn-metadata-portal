package au.edu.ardc.igsn.igsnportal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RenderService.class })
public class RenderServiceTest {

	@Autowired
	RenderService renderService;

	@Test
	@DisplayName("When provided with an ISO date string return DD MMM YYYY formatted date string")
	void testRenderDateYYYYMMDD() throws ParseException {

		String ISODate = "2020-10-28";
		String expectedFormat = "28 Oct 2020";

		assertThat(renderService.renderDate(ISODate)).isEqualTo(expectedFormat);
	}

	@Test
	@DisplayName("When provided with an YYYY-MM string return MMM YYYY formatted date string")
	void testRenderDateYYYYMM(){
		String ISODate = "2020-10";
		String expectedFormat = "Oct 2020";

		assertThat(renderService.renderDate(ISODate)).isEqualTo(expectedFormat);
	}

	@Test
	@DisplayName("When provided with an YYYY string return YYYY formatted date string")
	void testRenderDateYYYY() {

		String ISODate = "2020";
		String expectedFormat = "2020";

		assertThat(renderService.renderDate(ISODate)).isEqualTo(expectedFormat);
	}



}
