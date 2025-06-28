package individual.donationservice.controller.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CompletePaymentResponse {
    private String message;
    private String paymentStatus;
}
