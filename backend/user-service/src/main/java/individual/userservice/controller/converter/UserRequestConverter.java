package individual.userservice.controller.converter;

import individual.userservice.controller.dto.user.CreateUserRequest;
import individual.userservice.controller.dto.user.CreateUserResponse;
import individual.userservice.controller.dto.user.GetUsersResponse;
import individual.userservice.controller.dto.user.UpdateUserRequest;
import individual.userservice.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class UserRequestConverter {
    private UserRequestConverter() {}

    public static User convertCreateRequest(CreateUserRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    public static CreateUserResponse convertCreateResponse(Long id, String message){
        return CreateUserResponse.builder()
                .id(id)
                .message(message)
                .build();
    }

    public static User convertUpdateRequest(UpdateUserRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .build();
    }

    public static GetUsersResponse convertGetUsersResponse(List<User> users){
        return GetUsersResponse.builder()
                .users(users)
                .build();
    }

}
