package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.exception.NotFoundException;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import au.edu.ardc.igsn.igsnportal.response.ErrorResponse;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
        OkHttpClient client = getClient();
        HttpUrl url = HttpUrl.parse(baseUrl + "api/public/identifiers/").newBuilder()
                .addQueryParameter("page", Integer.toString(page))
                .addQueryParameter("size", Integer.toString(size))
                .build();
        Request request = new Request.Builder().url(url).build();

        PaginatedIdentifiersResponse recordsResponse = null;
        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            recordsResponse = objectMapper.readValue(response.body().string(), PaginatedIdentifiersResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordsResponse;
    }

    public String getContentForIdentifierValue(String identifier) throws IOException {
        OkHttpClient client = getClient();
        HttpUrl url = HttpUrl.parse(baseUrl + "api/public/igsn-description/").newBuilder()
                .addQueryParameter("identifier", identifier)
                .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.code() == 404) {
            String exceptionString = response.body().string();
            ObjectMapper objMapper = new ObjectMapper();
            ErrorResponse errorResponse = objMapper.readValue(exceptionString, ErrorResponse.class);
            throw new NotFoundException(errorResponse.message);
        }

        return response.body().string();
    }

    public boolean canEdit(String identifierValue, String accessToken) throws IOException {
        logger.debug(String.format("Attempting to find out if user can edit record, identifierValue:%s accessToken:%s", identifierValue, accessToken));
        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url(baseUrl + "api/services/auth-check/?identifier="+identifierValue)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Response response = client.newCall(request).execute();
        logger.debug(String.format("Response Code: %s", response.code()));
        logger.debug(String.format("Response Body: %s", response.body().string()));
        if (response.code() == 200) {
            return true;
        }

        return false;
    }

    public OkHttpClient getClient() {
        return new OkHttpClient.Builder().build();
    }

}
