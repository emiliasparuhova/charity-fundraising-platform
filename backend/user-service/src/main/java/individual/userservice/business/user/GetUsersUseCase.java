package individual.userservice.business.user;

import individual.userservice.domain.User;

import java.util.List;

public interface GetUsersUseCase {
    List<User> getUsers();
}
