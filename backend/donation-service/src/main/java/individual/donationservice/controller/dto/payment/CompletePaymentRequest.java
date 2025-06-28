package individual.donationservice.controller.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CompletePaymentRequest {
    private String token;
    private String payerId;
}
