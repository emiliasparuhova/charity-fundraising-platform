package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.GetUsersUseCase;
import individual.userservice.domain.User;
import individual.userservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll().stream()
                .map(UserConverter::convertToDomain)
                .toList();
    }
}
