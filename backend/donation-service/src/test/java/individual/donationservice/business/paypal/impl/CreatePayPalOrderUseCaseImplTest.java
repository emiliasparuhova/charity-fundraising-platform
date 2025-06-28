package individual.donationservice.business.paypal.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePayPalOrderUseCaseImplTest {

    @Mock
    RestTemplate restTemplateMock;

    @InjectMocks
    CreatePayPalOrderUseCaseImpl createPayPalOrderUseCase;

    @BeforeEach
    void setup() throws Exception {
        Field apiBaseField = CreatePayPalOrderUseCaseImpl.class.getDeclaredField("apiBase");
        apiBaseField.setAccessible(true);
        apiBaseField.set(createPayPalOrderUseCase, "https://api.paypal.com");

        Field returnUrlField = CreatePayPalOrderUseCaseImpl.class.getDeclaredField("returnUrl");
        returnUrlField.setAccessible(true);
        returnUrlField.set(createPayPalOrderUseCase, "https://example.com/success");

        Field cancelUrlField = CreatePayPalOrderUseCaseImpl.class.getDeclaredField("cancelUrl");
        cancelUrlField.setAccessible(true);
        cancelUrlField.set(createPayPalOrderUseCase, "https://example.com/cancel");
    }

    @Test
    void createOrder_returnsApprovalUrl_whenResponseContainsApprovalLink() throws Exception {
        String accessToken = "mockAccessToken";
        String paypalEmail = "mock@paypal.com";
        double amount = 15.5;

        String approvalUrl = "https://www.paypal.com/checkoutnow?token=12345";

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = """
            {
              "links": [
                {"href": "https://api.paypal.com/other", "rel": "self"},
                {"href": "%s", "rel": "approve"}
              ]
            }
            """.formatted(approvalUrl);
        JsonNode responseBody = mapper.readTree(jsonResponse);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        String result = createPayPalOrderUseCase.createOrder(accessToken, paypalEmail, amount);

        assertEquals(approvalUrl, result);
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void createOrder_throwsException_whenLinksNotPresent() {
        String accessToken = "mockAccessToken";
        String paypalEmail = "mock@paypal.com";
        double amount = 10.0;

        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("links")).thenReturn(false);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.CREATED);
        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createPayPalOrderUseCase.createOrder(accessToken, paypalEmail, amount));

        assertTrue(exception.getMessage().contains("Unexpected response from PayPal"));
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void createOrder_throwsException_whenApprovalLinkNotFound() throws Exception {
        String accessToken = "mockAccessToken";
        String paypalEmail = "mock@paypal.com";
        double amount = 25.0;

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = """
            {
              "links": [
                {"href": "https://api.paypal.com/other", "rel": "self"}
              ]
            }
            """;
        JsonNode responseBody = mapper.readTree(jsonResponse);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createPayPalOrderUseCase.createOrder(accessToken, paypalEmail, amount));

        assertEquals("Approval URL not found in PayPal response.", exception.getMessage());
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }
}
