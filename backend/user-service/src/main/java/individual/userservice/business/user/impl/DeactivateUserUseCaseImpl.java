package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.DeactivateUserUseCase;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.User;
import individual.userservice.messaging.MessageSender;
import individual.userservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DeactivateUserUseCaseImpl implements DeactivateUserUseCase {
    private final UserRepository userRepository;
    private final MessageSender messageSender;

    public void deactivateUser(Long userId) {
        User user = UserConverter.convertToDomain(
                userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));

        user.setProfilePicture(null);
        user.setRole(null);
        user.setJoinDate(null);
        user.setIsDeleted(true);
        user.setDeletionDate(LocalDate.now());

        userRepository.save(UserConverter.convertToEntity(user, null));

        messageSender.sendUserDeleted(userId);
    }
}
