package individual.userservice.business.user;

import individual.userservice.domain.User;

public interface GetUserUseCase {
    User getUser(Long id);
}
