package com.scheduler.content_scheduler.integrations.twitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TwitterService {

    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public TwitterService(OkHttpClient client) {
        this.client = client;
    }

    public TwitterProfileDTO getMe(String accessToken) throws IOException {

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/users/me")
                .get()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Twitter API error");
            }

            JsonNode root = mapper.readTree(response.body().string());
            JsonNode data = root.get("data");

            return new TwitterProfileDTO(
                    data.get("id").asText(),
                    data.get("username").asText(),
                    data.get("name").asText()
            );
        }
    }
}

