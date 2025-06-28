package individual.donationservice.controller.dto.donation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateDonationRequest {
    @DecimalMin(value = "0.0", message = "The amount must be a positive number")
    private Long amount;
    @NotNull(message = "The charity ID is mandatory")
    private Long charityId;
    @NotNull(message = "The donor ID is mandatory")
    private Long donorId;
}
