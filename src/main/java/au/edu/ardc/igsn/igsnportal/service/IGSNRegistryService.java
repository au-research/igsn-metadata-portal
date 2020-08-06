package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IGSNRegistryService {

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

    public String getContentForIdentifierValue(String identifier) {
        OkHttpClient client = getClient();
        HttpUrl url = HttpUrl.parse(baseUrl + "api/public/igsn-description/").newBuilder()
                .addQueryParameter("identifier", identifier)
                .build();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo throw exception here
        return "";
    }

    public OkHttpClient getClient() {
        return new OkHttpClient.Builder().build();
    }

}
