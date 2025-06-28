package individual.charityservice.domain;

import individual.charityservice.domain.enums.CharityCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Charity {
    private Long id;
    private String title;
    private String description;
    private double fundraisingGoal;
    private Long creatorId;
    private String paypalEmail;
    private LocalDate creationDate;
    private List<byte[]> photos;
    private CharityCategory category;
    private Boolean isActive;
}
