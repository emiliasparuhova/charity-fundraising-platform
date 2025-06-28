package individual.userservice.business.converter;

import individual.userservice.persistence.entity.CredentialsEntity;

import java.util.Objects;

public final class CredentialsConverter {

    private CredentialsConverter(){

    }

    public static CredentialsEntity convertToEntity(String hashedPassword){
        if (Objects.isNull(hashedPassword)){
            return CredentialsEntity.builder().build();
        }
        return CredentialsEntity.builder()
                .hashedPassword(hashedPassword)
                .build();
    }
}
