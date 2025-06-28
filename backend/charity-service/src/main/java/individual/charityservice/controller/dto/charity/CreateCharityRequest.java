package individual.charityservice.controller.dto.charity;

import individual.charityservice.domain.enums.CharityCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateCharityRequest {
    @NotBlank(message = "The title is mandatory")
    private String title;
    @NotBlank(message = "The description is mandatory")
    private String description;
    @DecimalMin(value = "0.0", message = "The fundraising goal must be a positive number")
    private double fundraisingGoal;
    @NotNull(message = "The creator ID is mandatory")
    private Long creatorId;
    @NotBlank(message = "The PayPal email is mandatory.")
    private String paypalEmail;
    private List<String> photos;
    @NotNull(message = "The category field is mandatory")
    private CharityCategory category;
}
