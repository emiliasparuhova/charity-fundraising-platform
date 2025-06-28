package individual.donationservice.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DonationStats {
    private Long donationCount;
    private Double totalFundsRaised;
}
