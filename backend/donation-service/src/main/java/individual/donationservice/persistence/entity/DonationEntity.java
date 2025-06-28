package individual.donationservice.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "donation", schema = "public")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "charity_id", nullable = false)
    private Long charityId;

    @Column(name = "donor_id", nullable = false)
    private Long donorId;
}
