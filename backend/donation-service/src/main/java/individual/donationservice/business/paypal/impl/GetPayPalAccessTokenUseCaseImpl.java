package individual.donationservice.business.paypal.impl;

import com.fasterxml.jackson.databind.JsonNode;
import individual.donationservice.business.paypal.GetPayPalAccessTokenUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class GetPayPalAccessTokenUseCaseImpl implements GetPayPalAccessTokenUseCase {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.api-base}")
    private String apiBase;

    private final RestTemplate restTemplate;

    public GetPayPalAccessTokenUseCaseImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getPayPalAccessToken() {
        String url = apiBase + "/v1/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String creds = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        headers.set("Authorization", "Basic " + creds);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class);

        JsonNode responseBody = response.getBody();
        if (responseBody == null || !responseBody.has("access_token")) {
            throw new RuntimeException("Access token not found in PayPal response: " + responseBody);
        }

        return responseBody.get("access_token").asText();
    }
}
