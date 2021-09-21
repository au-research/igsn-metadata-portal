package au.edu.ardc.igsn.igsnportal;

import au.edu.ardc.igsn.igsnportal.model.igsn.Curation;
import au.edu.ardc.igsn.igsnportal.model.igsn.RelatedResource;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resource;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ARDCv1DeserialiseTest {

	@Test
	void deserialise_samplev1() throws IOException {
		String xml = TestHelper.readFile("src/test/resources/xml/sample_ardc_v1.xml");
		XmlMapper xmlMapper = new XmlMapper();
		Resources resources = xmlMapper.readValue(xml, Resources.class);
		Resource resource = resources.resource;

		assertThat(resource.registeredObjectType)
				.isEqualTo("http://pid.geoscience.gov.au/def/voc/ga/igsncode/PhysicalSample");
		assertThat(resource.resourceIdentifier).isEqualTo("10273/XX0TUIAYLV");
		assertThat(resource.landingPage).isEqualTo("https://demo.identifiers.ardc.edu.au/igsn/#/meta/XX0TUIAYLV");
		assertThat(resource.isPublic).isEqualTo("true");
		assertThat(resource.resourceTitle).isEqualTo("This Tiltle also left blank on purpose");
		assertThat(resource.purpose).isEqualTo("Purpose");
		assertThat(resource.campaign).isEqualTo("Project");
		assertThat(resource.comments).isEqualTo("Comments");

		// todo test alternateIdentifiers
		assertThat(resource.alternateIdentifiers).hasSize(2);
		assertThat(resource.alternateIdentifiers.get(0).value).isEqualTo("AltID2134");
		assertThat(resource.alternateIdentifiers.get(1).value).isEqualTo("AltID2135");

		// materialTypes
		assertThat(resource.materialTypes).hasSize(1);
		assertThat(resource.materialTypes.get(0).value).isEqualTo("http://vocabulary.odm2.org/medium/liquidOrganic");

		// todo test classifications

		// location
		assertThat(resource.location).isNotNull();
		assertThat(resource.location.locality).isNotNull();
		assertThat(resource.location.locality.localityURI).isEqualTo("locality URL");

		// date
		assertThat(resource.date).isNotNull();
		assertThat(resource.date.timeInstant).isNull();
		assertThat(resource.date.timePeriod).isNotNull();
		assertThat(resource.date.timePeriod.start).isEqualTo("2020-07-30");
		assertThat(resource.date.timePeriod.end).isEqualTo("2020-07-30");

		// method
		assertThat(resource.method).isNotNull();
		assertThat(resource.method.value).isEqualTo("Method");
		assertThat(resource.method.methodURI).isEqualTo("MethodURL");

		// logDate
		assertThat(resource.logDate).isNotNull();
		assertThat(resource.logDate.eventType).isEqualTo("updated");

		// curationDetails
		assertThat(resource.curationDetails).hasSize(1);
		assertThat(resource.curationDetails.get(0).curator).isNotNull();
		assertThat(resource.curationDetails.get(0).curator.curatorName).isEqualTo("Joel Benn");
		assertThat(resource.curationDetails.get(0).curator.curatorIdentifier).hasSize(1);
		assertThat(resource.curationDetails.get(0).curator.curatorIdentifier.get(0).curatorIdentifierType)
				.isEqualTo("http://pid.geoscience.gov.au/def/voc/ga/igsncode/DOI");

		// contributors
		assertThat(resource.contributors).hasSize(2);
		assertThat(resource.contributors.get(0).contributorType)
				.isEqualTo("http://registry.it.csiro.au/def/isotc211/CI_RoleCode/pointOfContact");
		assertThat(resource.contributors.get(0).contributorName).isEqualTo("Sarah Contributor");
		assertThat(resource.contributors.get(0).contributorIdentifier).isNull();
		assertThat(resource.contributors.get(1).contributorIdentifier).hasSize(1);
		assertThat(resource.contributors.get(1).contributorIdentifier.get(0).contributorIdentifierType)
				.isEqualTo("http://pid.geoscience.gov.au/def/voc/ga/igsncode/ORCID");

		// relatedResources
		List<RelatedResource> relatedResources = resource.relatedResources;
		assertThat(relatedResources).hasSize(1);
		RelatedResource relatedResource = relatedResources.get(0);
		assertThat(relatedResource.relationType)
				.isEqualTo("http://pid.geoscience.gov.au/def/voc/ga/igsncode/HasReferenceResource");
		assertThat(relatedResource.relatedResourceTitle).isEqualTo("Example Publication");
		assertThat(relatedResource.relatedResourceIdentifier).hasSize(1);
		// assertThat(relatedResource.relatedResourceIdentifier.get(0).value).isEqualTo("eissn-sdoiuowre");
		assertThat(relatedResource.relatedResourceIdentifier.get(0).relatedResourceIdentifierType)
				.isEqualTo("http://pid.geoscience.gov.au/def/voc/ga/igsncode/EISSN");

		assertThat(resources).isNotNull();
	}

}
