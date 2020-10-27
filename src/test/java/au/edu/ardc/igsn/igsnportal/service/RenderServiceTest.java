package au.edu.ardc.igsn.igsnportal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RenderService.class })
public class RenderServiceTest {

	@Autowired
	RenderService renderService;

	@Test
	@DisplayName("When provided with an ISO date string return DD MMM YYYY formatted date string")
	void testRenderDate() {

		String ISODate = "2020-10-28";
		String expectedFormat = "28 Oct 2020";

		assertThat(renderService.renderDate(ISODate)).isEqualTo(expectedFormat);
	}

}
