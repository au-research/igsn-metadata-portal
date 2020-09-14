package au.edu.ardc.igsn.igsnportal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { VocabService.class })
class VocabServiceTest {

	@Autowired
	VocabService vocabService;

	@Test
    @DisplayName("Provides the correct terms for a given URI")
	void getVocabTermForURI() {
	    // standard resolve ardcv1 with lookup table
	    assertThat(vocabService.resolve("http://vocabulary.odm2.org/medium/other")).isEqualTo("Other");

	    // return raw when not resolvable
        assertThat(vocabService.resolve("NOTRESOLVABLE")).isEqualTo("NOTRESOLVABLE");
	}

	@Test
	@DisplayName("Upon initialization, ardcv1 vocab should already populate the lookup table")
	void loadLocalARDCv1Vocab() {
		assertThat(vocabService.getLookupTable()).isNotNull();
		assertThat(vocabService.getLookupTable().isEmpty()).isFalse();

		// sanity check, there should be more than 50 terms
       assertThat(vocabService.getLookupTable().size()).isGreaterThan(50);
	}

}