package individual.charityservice.controller.dto.charity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateCharityResponse {
    private Long id;
}
