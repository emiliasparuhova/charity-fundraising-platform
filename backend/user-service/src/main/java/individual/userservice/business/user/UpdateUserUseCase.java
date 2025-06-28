package individual.userservice.business.user;

import individual.userservice.domain.User;

public interface UpdateUserUseCase {
    void updateUser(Long userId, User updatedUserData);
}
