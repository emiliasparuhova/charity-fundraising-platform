package individual.donationservice.controller;

import individual.donationservice.business.paypal.CompletePayPalOrderUseCase;
import individual.donationservice.business.paypal.CreatePayPalOrderUseCase;
import individual.donationservice.business.paypal.GetPayPalAccessTokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetPayPalAccessTokenUseCase getPayPalAccessTokenUseCase;

    @MockitoBean
    private CreatePayPalOrderUseCase createPayPalOrderUseCase;

    @MockitoBean
    private CompletePayPalOrderUseCase completePayPalOrderUseCase;

    @Test
    void createPayPalOrder_shouldReturn200_whenOrderCreated() throws Exception {
        when(getPayPalAccessTokenUseCase.getPayPalAccessToken()).thenReturn("access_token");
        when(createPayPalOrderUseCase.createOrder("access_token", "email@paypal.com", 100.0)).thenReturn("http://approval.url");

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "amount": 100.0,
                            "paypalEmail": "email@paypal.com"
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.approvalUrl").value("http://approval.url"));
    }

    @Test
    void createPayPalOrder_shouldReturn500_whenErrorOccurs() throws Exception {
        when(getPayPalAccessTokenUseCase.getPayPalAccessToken()).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "amount": 100.0,
                            "paypalEmail": "email@paypal.com"
                        }
                    """))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void completePayPalOrder_shouldReturn200_whenPaymentCompleted() throws Exception {
        when(getPayPalAccessTokenUseCase.getPayPalAccessToken()).thenReturn("access_token");
        when(completePayPalOrderUseCase.completePayment("access_token", "token123", "payerId123")).thenReturn("Completed");

        mockMvc.perform(post("/payments/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "token123",
                            "payerId": "payerId123"
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Payment successful!"))
                .andExpect(jsonPath("$.paymentStatus").value("Completed"));
    }

    @Test
    void completePayPalOrder_shouldReturn500_whenErrorOccurs() throws Exception {
        when(getPayPalAccessTokenUseCase.getPayPalAccessToken()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/payments/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "token123",
                            "payerId": "payerId123"
                        }
                    """))
                .andExpect(status().isInternalServerError());
    }
}
