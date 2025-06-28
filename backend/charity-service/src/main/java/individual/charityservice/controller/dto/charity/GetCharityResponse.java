package individual.charityservice.controller.dto.charity;

import individual.charityservice.domain.enums.CharityCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class GetCharityResponse {
    private Long id;
    private String title;
    private String description;
    private double fundraisingGoal;
    private double fundsRaised;
    private Long creatorId;
    private String paypalEmail;
    private LocalDate creationDate;
    private List<byte[]> photos;
    private CharityCategory category;
    private Boolean isActive;
}
