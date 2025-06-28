package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.UpdateUserUseCase;
import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.User;
import individual.userservice.persistence.UserRepository;
import individual.userservice.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepository userRepository;

    public void updateUser(Long userId, User updatedUserData) {
        UserEntity existingUserEntity = (userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));

        User existingUser = UserConverter.convertToDomain(existingUserEntity);
        String hashedPassword = existingUserEntity.getCredentials().getHashedPassword();

        if (!Objects.equals(updatedUserData.getEmail(), existingUser.getEmail()) &&
                userRepository.existsByEmailAndIsDeletedFalse(updatedUserData.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        existingUser.setName(updatedUserData.getName());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setProfilePicture(updatedUserData.getProfilePicture());

        userRepository.save(UserConverter.convertToEntity(existingUser, hashedPassword));
    }
}
