package individual.charityservice.controller.dto.charity;

import individual.charityservice.domain.Charity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class GetCharitiesResponse {
    private List<Charity> charities;
}
