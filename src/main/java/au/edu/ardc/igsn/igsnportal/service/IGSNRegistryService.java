package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.config.ApplicationProperties;
import au.edu.ardc.igsn.igsnportal.response.ErrorResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedRecordsResponse;
import au.edu.ardc.igsn.igsnportal.util.Helpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class IGSNRegistryService {

	public static final String ARDCv1 = "ardc-igsn-desc-1.0";

	public static final String ARDCv1JSONLD = "ardc-igsn-desc-1.0-jsonld";

	final ApplicationProperties applicationProperties;

	Logger logger = LoggerFactory.getLogger(IGSNRegistryService.class);

	public IGSNRegistryService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public PaginatedRecordsResponse getPublicRecords(int page, int size) {
		String registryUrl = applicationProperties.getRegistryUrl();

		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(registryUrl + "api/public/records/").newBuilder()
				.addQueryParameter("page", Integer.toString(page)).addQueryParameter("size", Integer.toString(size))
				.addQueryParameter("type", "IGSN").build();
		Request request = new Request.Builder().url(url).build();

		PaginatedRecordsResponse recordsResponse = null;
		try (Response response = client.newCall(request).execute()) {
			ObjectMapper objectMapper = new ObjectMapper();
			recordsResponse = objectMapper.readValue(response.body().string(), PaginatedRecordsResponse.class);
		}
		catch (IOException e) {
			logger.error("Exception while getting Public Records:" + e.getMessage());
			//e.printStackTrace();
		}

		return recordsResponse;
	}

	public PaginatedIdentifiersResponse getPublicIdentifiers(int page, int size) {
		logger.debug(String.format("Obtaining content for public identifiers page %s size %s ", page, size));
		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(applicationProperties.getRegistryUrl() + "api/public/identifiers/").newBuilder()
				.addQueryParameter("page", Integer.toString(page)).addQueryParameter("size", Integer.toString(size))
				.build();
		logger.debug(String.format("GET url:", url.toString()));
		Request request = new Request.Builder().url(url).build();

		PaginatedIdentifiersResponse recordsResponse = null;
		try (Response response = client.newCall(request).execute()) {
			logger.debug(String.format("Response received, code: %s, length:%s", response.code(),
					response.body().contentLength()));
			ObjectMapper objectMapper = new ObjectMapper();
			recordsResponse = objectMapper.readValue(response.body().string(), PaginatedIdentifiersResponse.class);
		}
		catch (IOException e) {
			logger.error("Exception while deserialize getPublicIdentifiers message:" + e.getMessage());
			//e.printStackTrace();
		}
		return recordsResponse;
	}

	/**
	 * Uses the Public IGSN Description API to obtain XML or JSONLD of a given
	 * identifierValue
	 * @param identifier the identifierValue in the form of prefix/namespace+value eg.
	 * 10273/XX0TUIAYLV
	 * @param schema the schemaID of the requested version
	 * @return string of the response
	 * @throws IOException when requests fail
	 */
	public String getContentForIdentifierValue(String identifier, String schema) throws IOException {

		// this section below is used to render test record for UI development
		if (identifier.equals("test/full") && schema.equals(ARDCv1)) {
			return Helpers.readFile("src/test/resources/xml/sample_ardc_v1.xml");
		}
		else if (identifier.equals("test/badWkt") && schema.equals(ARDCv1)) {
			return Helpers.readFile("src/test/resources/xml/sample_ardc_v1_badWkt.xml");
		}

		logger.debug("Obtaining content for identifier: " + identifier);
		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(applicationProperties.getRegistryUrl() + "api/public/igsn-description/")
				.newBuilder().addQueryParameter("identifier", identifier).addQueryParameter("schema", schema).build();
		logger.debug("GET url: " + url.toString());
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		logger.debug(String.format("Response received, code: %s, length: %s", response.code(),
				response.body().contentLength()));
		if (response.code() == 404) {
			logger.debug("Not found exception");
			String exceptionString = response.body().string();
			ObjectMapper objMapper = new ObjectMapper();
			ErrorResponse errorResponse = objMapper.readValue(exceptionString, ErrorResponse.class);
			logger.debug("Mapped error response, message" + errorResponse.message);
			return "";
		}

		return response.body().string();
	}

	/**
	 * Return if a provided AccessToken can edit the identifier using the auth-check API
	 * provided by the registry
	 * @param identifierValue the identifierValue eg 10273/XX0TUIAYLV
	 * @param accessToken the accessToken obtained from the current {@link UserService}
	 * request
	 * @return true or false depends on if the user has access to edit the IGSN identifier
	 * @throws IOException when requests fail
	 */
	public boolean canEdit(String identifierValue, String accessToken) throws IOException {
		logger.debug(String.format("Attempting to find out if user can edit record, identifierValue:%s accessToken:%s",
				identifierValue, accessToken));
		OkHttpClient client = getClient();
		Request request = new Request.Builder()
				.url(applicationProperties.getRegistryUrl() + "api/services/auth-check/?identifier=" + identifierValue)
				.addHeader("Authorization", "Bearer " + accessToken).build();
		Response response = client.newCall(request).execute();
		logger.debug(String.format("Response Code: %s", response.code()));
		logger.debug(String.format("Response Body: %s", response.body().string()));
		return response.code() == 200;
	}


	/**
	 * Return if a provided AccessToken can edit the identifier using the auth-check API
	 * provided by the registry
	 * @param identifierValue the identifierValue eg 10273/XX0TUIAYLV
	 * request
	 * @return true or false depends on if IGSN is public or private
	 * @throws IOException when requests fail
	 */
	public boolean isPublic(String identifierValue) throws IOException {
		logger.debug(String.format("Attempting to find out if theIGSN is public, identifierValue:%s",
				identifierValue));
		OkHttpClient client = getClient();
		Request request = new Request.Builder()
				.url(applicationProperties.getRegistryUrl() + "api/services/isPublic/?identifier=" + identifierValue)
				.build();
		Response response = client.newCall(request).execute();
		return response.code() == 200;
	}

	/**
	 * Return if a provided AccessToken can edit the identifier using the auth-check API
	 * provided by the registry
	 * @param identifierValue the identifierValue eg 10273/XX0TUIAYLV
	 * @return return response with the embargoEnd as string date in body if it exists
	 * @throws IOException when requests fail
	 */
	public Response hasEmbargo(String identifierValue) throws IOException {
		OkHttpClient client = getClient();
		Request request = new Request.Builder()
				.url(applicationProperties.getRegistryUrl() + "api/services/hasEmbargo/?identifier=" + identifierValue)
				.build();
		Response response = client.newCall(request).execute();
		return response;
	}

	public OkHttpClient getClient() {
		return new OkHttpClient.Builder().build();
	}

	/**
	 * Check if an identifierValue is a TEST IGSN. Current business rule only require the
	 * prefix check for now
	 * @since 1.0
	 * @param identifierValue the IGSN value in the form of prefix/igsn
	 * @return true if the identifierValue is a Test one, false if not
	 *
	 */
	public boolean isTestIGSN(String identifierValue) {
		String[] values = identifierValue.split("/");
		String prefix = values[0];
		return prefix.equals("20.500.11812");
	}


	/**
	 * This URL will be used to display in the page as well as generating QR Code
	 * @param identifierValue in the form of prefix/igsn
	 * @return String URL of the IGSN URL
	 */
	public String getIGSNURL(String identifierValue) {
		// test IGSN will have the handle value instead
		if (isTestIGSN(identifierValue)) {
			return String.format("http://hdl.handle.net/%s", identifierValue);
		}

		// production IGSN will only have the value bit
		String[] values = identifierValue.split("/");
		return String.format("http://igsn.org/%s", values[1]);
	}

	/**
	 * Obtain the URL to edit identifier. Identifier - record - version the url would be
	 * {app.editor.url}/#/edit/{schema}/{version.id}
	 * @param identifierValue the identifier string
	 * @return the URL to edit the identifier
	 */
	public String getEditIGSNLink(String identifierValue) {
		return String.format("%s/#/edit/%s/%s", applicationProperties.getEditorUrl(), IGSNRegistryService.ARDCv1, identifierValue);
	}

	public String getEditorLink() {
		return String.format(applicationProperties.getEditorUrl());
	}

	public String getVersionStatus(String identifierValue, String schema) throws IOException {

		OkHttpClient client = getClient();
		Request request = new Request.Builder()
				.url(applicationProperties.getRegistryUrl() +
						"api/services/getVersionStatus/?identifier=" + identifierValue + "&schema=" + schema)
				.build();
		Response response = client.newCall(request).execute();
		String identifierStatus = response.body().string();
		return identifierStatus;

	}
}
