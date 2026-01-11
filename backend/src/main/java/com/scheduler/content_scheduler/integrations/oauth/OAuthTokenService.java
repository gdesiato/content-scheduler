package com.scheduler.content_scheduler.integrations.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OAuthTokenService {

    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public OAuthTokenService(OkHttpClient client) {
        this.client = client;
    }

    public JsonNode exchangeCode(
            OAuthProvider provider,
            String code,
            String codeVerifier
    ) throws IOException {

        FormBody.Builder form = new FormBody.Builder();
        provider.buildTokenRequestParams(code, codeVerifier)
                .forEach(form::add);

        Request request = new Request.Builder()
                .url(provider.getTokenEndpoint())
                .post(form.build())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(response.body().string());
            }
            return mapper.readTree(response.body().string());
        }
    }
}
