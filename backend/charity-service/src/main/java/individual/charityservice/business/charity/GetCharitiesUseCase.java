package individual.charityservice.business.charity;

import individual.charityservice.domain.Charity;

import java.util.List;

public interface GetCharitiesUseCase {
    List<Charity> getCharities();
}
