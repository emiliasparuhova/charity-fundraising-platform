package individual.userservice.business.converter;

import individual.userservice.domain.User;
import individual.userservice.persistence.entity.CredentialsEntity;
import individual.userservice.persistence.entity.UserEntity;

import java.util.Objects;

public final class UserConverter {

    private UserConverter(){

    }

    public static User convertToDomain(UserEntity userEntity){
        if (Objects.isNull(userEntity)) {
            return User.builder().build();
        }

        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .profilePicture(userEntity.getProfilePicture())
                .joinDate(userEntity.getJoinDate())
                .role(userEntity.getRole())
                .isDeleted(userEntity.getIsDeleted())
                .deletionDate(userEntity.getDeletionDate())
                .build();
    }

    public static UserEntity convertToEntity(User user, String hashedPassword){
        if (Objects.isNull(user)) {
            return UserEntity.builder()
                    .credentials(CredentialsEntity.builder().build())
                    .build();
        }

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .credentials(CredentialsConverter.convertToEntity(hashedPassword))
                .profilePicture(user.getProfilePicture())
                .joinDate(user.getJoinDate())
                .role(user.getRole())
                .isDeleted(user.getIsDeleted())
                .deletionDate(user.getDeletionDate())
                .build();
    }
}
