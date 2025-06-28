package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.CreateUserUseCase;
import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long createUser(User user, String plainTextPassword) {
        if (userRepository.existsByEmailAndIsDeletedFalse(user.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        String hashedPassword = passwordEncoder.encode(plainTextPassword);
        user.setJoinDate(LocalDate.now());
        user.setRole(UserRole.USER);

        user.setIsDeleted(false);
        user.setDeletionDate(null);

        return userRepository.save(UserConverter.convertToEntity(user, hashedPassword)).getId();
    }
}
