package individual.donationservice.business.paypal.impl;

import com.fasterxml.jackson.databind.JsonNode;
import individual.donationservice.business.paypal.CreatePayPalOrderUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CreatePayPalOrderUseCaseImpl implements CreatePayPalOrderUseCase {

    @Value("${paypal.api-base}")
    private String apiBase;

    @Value("${paypal.return-url}")
    private String returnUrl;

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    private final RestTemplate restTemplate;

    public CreatePayPalOrderUseCaseImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createOrder(String accessToken, String paypalEmail, double amount) {
        String url = apiBase + "/v2/checkout/orders";
        String amountStr = String.format("%.2f", amount).replace(",", ".");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(
                        Map.of(
                                "amount", Map.of("currency_code", "EUR", "value", amountStr),
                                "payee", Map.of("email_address", paypalEmail)
                        )
                ),
                "application_context", Map.of(
                        "return_url", returnUrl,
                        "cancel_url", cancelUrl
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);

        JsonNode responseBody = response.getBody();
        if (responseBody == null || !responseBody.has("links")) {
            throw new RuntimeException("Unexpected response from PayPal: " + responseBody);
        }

        for (JsonNode link : responseBody.get("links")) {
            if ("approve".equals(link.get("rel").asText())) {
                return link.get("href").asText();
            }
        }

        throw new RuntimeException("Approval URL not found in PayPal response.");
    }
}
