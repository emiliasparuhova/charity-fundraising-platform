package individual.donationservice.business.paypal.impl;

import com.fasterxml.jackson.databind.JsonNode;
import individual.donationservice.business.paypal.CompletePayPalOrderUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CompletePayPalOrderUseCaseImpl implements CompletePayPalOrderUseCase {
    @Value("${paypal.api-base}")
    private String apiBase;

    private final RestTemplate restTemplate;

    public CompletePayPalOrderUseCaseImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String completePayment(String accessToken, String token, String payerId) {
        String url = apiBase + "/v2/checkout/orders/" + token + "/capture";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = Map.of(
                "payer_id", payerId
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);

        JsonNode responseBody = response.getBody();

        if (responseBody != null && responseBody.has("status")) {
            String status = responseBody.get("status").asText();
            if ("COMPLETED".equals(status)) {
                return "COMPLETED";
            } else {
                return status;
            }
        } else {
            throw new RuntimeException("Unexpected response from PayPal: " + responseBody);
        }
    }
}
