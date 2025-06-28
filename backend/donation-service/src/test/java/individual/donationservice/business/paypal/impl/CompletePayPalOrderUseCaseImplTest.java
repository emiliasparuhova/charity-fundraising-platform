package individual.donationservice.business.paypal.impl;

import com.fasterxml.jackson.databind.JsonNode;
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
class CompletePayPalOrderUseCaseImplTest {

    @Mock
    RestTemplate restTemplateMock;

    @InjectMocks
    CompletePayPalOrderUseCaseImpl completePayPalOrderUseCase;

    @BeforeEach
    void setup() throws Exception {
        Field apiBaseField = CompletePayPalOrderUseCaseImpl.class.getDeclaredField("apiBase");
        apiBaseField.setAccessible(true);
        apiBaseField.set(completePayPalOrderUseCase, "https://api.paypal.com");
    }

    @Test
    void completePayment_returnsCompleted_whenStatusIsCompleted() {
        String accessToken = "mockAccessToken";
        String token = "mockToken";
        String payerId = "mockPayerId";

        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("status")).thenReturn(true);
        when(responseBodyMock.get("status")).thenReturn(mock(JsonNode.class));
        when(responseBodyMock.get("status").asText()).thenReturn("COMPLETED");

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.OK);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        String result = completePayPalOrderUseCase.completePayment(accessToken, token, payerId);

        assertEquals("COMPLETED", result);
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void completePayment_returnsOtherStatus_whenStatusIsNotCompleted() {
        String accessToken = "mockAccessToken";
        String token = "mockToken";
        String payerId = "mockPayerId";

        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("status")).thenReturn(true);
        when(responseBodyMock.get("status")).thenReturn(mock(JsonNode.class));
        when(responseBodyMock.get("status").asText()).thenReturn("PENDING");

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.OK);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        String result = completePayPalOrderUseCase.completePayment(accessToken, token, payerId);

        assertEquals("PENDING", result);
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void completePayment_throwsException_whenResponseIsUnexpected() {
        String accessToken = "mockAccessToken";
        String token = "mockToken";
        String payerId = "mockPayerId";

        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("status")).thenReturn(false);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.OK);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> completePayPalOrderUseCase.completePayment(accessToken, token, payerId));

        assertTrue(exception.getMessage().contains("Unexpected response from PayPal"));
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(JsonNode.class));
    }
}
