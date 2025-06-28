package individual.userservice.business.user.impl;

import individual.userservice.business.user.GetUserUseCase;
import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.User;
import individual.userservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private UserRepository userRepository;
    @Override
    public User getUser(Long id) {
        User user = userRepository.findById(id)
                .map(UserConverter::convertToDomain)
                .orElseThrow(UserNotFoundException::new);

        if (user.getIsDeleted()) {
            throw new UserNotFoundException();
        }

        return user;
    }
}
