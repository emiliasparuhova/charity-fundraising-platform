package individual.userservice.business.user;

import individual.userservice.domain.User;

public interface CreateUserUseCase {
    Long createUser(User user, String plainTextPassword);
}
