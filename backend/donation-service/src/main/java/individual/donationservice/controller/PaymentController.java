package individual.donationservice.controller;

import individual.donationservice.business.paypal.CompletePayPalOrderUseCase;
import individual.donationservice.business.paypal.CreatePayPalOrderUseCase;
import individual.donationservice.business.paypal.GetPayPalAccessTokenUseCase;
import individual.donationservice.controller.dto.payment.CompletePaymentRequest;
import individual.donationservice.controller.dto.payment.CompletePaymentResponse;
import individual.donationservice.controller.dto.payment.CreatePaymentRequest;
import individual.donationservice.controller.dto.payment.CreatePaymentResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
    private final GetPayPalAccessTokenUseCase getPayPalAccessTokenUseCase;
    private final CreatePayPalOrderUseCase createPayPalOrderUseCase;
    private final CompletePayPalOrderUseCase completePayPalOrderUseCase;

    @PostMapping()
    public ResponseEntity<CreatePaymentResponse> createPayPalOrder(@RequestBody CreatePaymentRequest request) {
        try{
            final String accessToken = getPayPalAccessTokenUseCase.getPayPalAccessToken();

            final CreatePaymentResponse response = CreatePaymentResponse.builder()
                    .approvalUrl(createPayPalOrderUseCase.createOrder(accessToken, request.getPaypalEmail(), request.getAmount()))
                    .build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<CompletePaymentResponse> completePayPalOrder(@RequestBody CompletePaymentRequest request) {
        try {
            final String accessToken = getPayPalAccessTokenUseCase.getPayPalAccessToken();
            String paymentStatus = completePayPalOrderUseCase.completePayment(accessToken, request.getToken(), request.getPayerId());

            CompletePaymentResponse response = CompletePaymentResponse.builder()
                    .message("Payment successful!")
                    .paymentStatus(paymentStatus)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
