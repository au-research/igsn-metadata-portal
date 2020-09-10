package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.exception.NotFoundException;
import au.edu.ardc.igsn.igsnportal.response.ErrorResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
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

	Logger logger = LoggerFactory.getLogger(IGSNRegistryService.class);

	@Value("${registry.url}")
	private String baseUrl;

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

	public String getContentForIdentifierValue(String identifier) throws IOException {
		logger.debug("Obtaining content for identifier: " + identifier);
		OkHttpClient client = getClient();
		HttpUrl url = HttpUrl.parse(baseUrl + "api/public/igsn-description/").newBuilder()
				.addQueryParameter("identifier", identifier).build();
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
			throw new NotFoundException(errorResponse.message);
		}

		return response.body().string();
	}

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
