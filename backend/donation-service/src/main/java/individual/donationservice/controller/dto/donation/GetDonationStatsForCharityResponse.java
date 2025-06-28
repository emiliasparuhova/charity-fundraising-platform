package individual.donationservice.controller.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class GetDonationStatsForCharityResponse {
    private Long donationCount;
    private Double totalFundsRaised;
}
