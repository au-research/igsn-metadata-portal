package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.response.ErrorResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedRecordsResponse;
import au.edu.ardc.igsn.igsnportal.util.Helpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IGSNRegistryService {

	public static final String ARDCv1 = "ardc-igsn-desc-1.0";

	public static final String ARDCv1JSONLD = "ardc-igsn-desc-1.0-jsonld";

	Logger logger = LoggerFactory.getLogger(IGSNRegistryService.class);

	@Value("${registry.url}")
	private String baseUrl;

	public PaginatedRecordsResponse getPublicRecords(int page, int size) {
		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(baseUrl + "api/public/records/").newBuilder()
				.addQueryParameter("page", Integer.toString(page)).addQueryParameter("size", Integer.toString(size))
				.addQueryParameter("type", "IGSN").build();
		Request request = new Request.Builder().url(url).build();

		PaginatedRecordsResponse recordsResponse = null;
		try (Response response = client.newCall(request).execute()) {
			ObjectMapper objectMapper = new ObjectMapper();
			recordsResponse = objectMapper.readValue(response.body().string(), PaginatedRecordsResponse.class);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return recordsResponse;
	}

	public PaginatedIdentifiersResponse getPublicIdentifiers(int page, int size) {
		logger.debug(String.format("Obtaining content for public identifiers page %s size %s ", page, size));
		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(baseUrl + "api/public/identifiers/").newBuilder()
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
			e.printStackTrace();
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
		HttpUrl url = HttpUrl.parse(baseUrl + "api/public/igsn-description/").newBuilder()
				.addQueryParameter("identifier", identifier).addQueryParameter("schema", schema).build();
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
		Request request = new Request.Builder().url(baseUrl + "api/services/auth-check/?identifier=" + identifierValue)
				.addHeader("Authorization", "Bearer " + accessToken).build();
		Response response = client.newCall(request).execute();
		logger.debug(String.format("Response Code: %s", response.code()));
		logger.debug(String.format("Response Body: %s", response.body().string()));
		return response.code() == 200;
	}

	public OkHttpClient getClient() {
		return new OkHttpClient.Builder().build();
	}

}
