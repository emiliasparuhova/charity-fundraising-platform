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
class GetPayPalAccessTokenUseCaseImplTest {

    @Mock
    RestTemplate restTemplateMock;

    @InjectMocks
    GetPayPalAccessTokenUseCaseImpl getPayPalAccessTokenUseCase;

    @BeforeEach
    void setup() throws Exception {
        Field clientIdField = GetPayPalAccessTokenUseCaseImpl.class.getDeclaredField("clientId");
        clientIdField.setAccessible(true);
        clientIdField.set(getPayPalAccessTokenUseCase, "mockClientId");

        Field clientSecretField = GetPayPalAccessTokenUseCaseImpl.class.getDeclaredField("clientSecret");
        clientSecretField.setAccessible(true);
        clientSecretField.set(getPayPalAccessTokenUseCase, "mockClientSecret");

        Field apiBaseField = GetPayPalAccessTokenUseCaseImpl.class.getDeclaredField("apiBase");
        apiBaseField.setAccessible(true);
        apiBaseField.set(getPayPalAccessTokenUseCase, "https://api.paypal.com");
    }

    @Test
    void getPayPalAccessToken_returnsToken_whenResponseIsValid() {
        String expectedToken = "mockAccessToken";

        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("access_token")).thenReturn(true);
        when(responseBodyMock.get("access_token")).thenReturn(mock(JsonNode.class));
        when(responseBodyMock.get("access_token").asText()).thenReturn(expectedToken);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.OK);

        when(restTemplateMock.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        String result = getPayPalAccessTokenUseCase.getPayPalAccessToken();

        assertEquals(expectedToken, result);
        verify(restTemplateMock, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void getPayPalAccessToken_throwsException_whenAccessTokenMissing() {
        JsonNode responseBodyMock = mock(JsonNode.class);
        when(responseBodyMock.has("access_token")).thenReturn(false);

        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseBodyMock, HttpStatus.OK);

        when(restTemplateMock.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getPayPalAccessTokenUseCase.getPayPalAccessToken());

        assertTrue(exception.getMessage().contains("Access token not found in PayPal response"));
        verify(restTemplateMock, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }

    @Test
    void getPayPalAccessToken_throwsException_whenResponseBodyIsNull() {
        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplateMock.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getPayPalAccessTokenUseCase.getPayPalAccessToken());

        assertTrue(exception.getMessage().contains("Access token not found in PayPal response"));
        verify(restTemplateMock, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }
}
