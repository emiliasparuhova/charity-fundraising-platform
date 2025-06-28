package individual.userservice.persistence;

import individual.userservice.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailAndIsDeletedFalse(String email);
    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);
}
