package individual.donationservice.domain;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Donation {
    private Long id;
    private double amount;
    private LocalDate date;
    private Long charityId;
    private Long donorId;
}
