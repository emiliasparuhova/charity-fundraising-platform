package individual.userservice.controller;

import individual.userservice.business.user.*;
import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.controller.converter.UserRequestConverter;
import individual.userservice.controller.dto.user.CreateUserRequest;
import individual.userservice.controller.dto.user.CreateUserResponse;
import individual.userservice.controller.dto.user.GetUsersResponse;
import individual.userservice.controller.dto.user.UpdateUserRequest;
import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final GetUserUseCase getUserUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long id){
        try{
            final User user = getUserUseCase.getUser(id);

            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    public ResponseEntity<GetUsersResponse> getUsers(@RequestHeader("X-Role") String role){
        try{
            if (!UserRole.ADMIN.name().equalsIgnoreCase(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            final GetUsersResponse response = UserRequestConverter.convertGetUsersResponse(
                getUsersUseCase.getUsers()
            );

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            final User user = UserRequestConverter.convertCreateRequest(request);
            final String plainTextPassword = request.getPassword();

            final CreateUserResponse response = UserRequestConverter.convertCreateResponse(
                    createUserUseCase.createUser(user, plainTextPassword), null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmailAlreadyInUseException e) {
            String errorMessage = "The email address is already in use.";
            CreateUserResponse response = UserRequestConverter.convertCreateResponse(null, errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") final long id,
                                           @RequestHeader("X-User-Id") String idFromAccessToken,
                                           @RequestBody @Valid UpdateUserRequest request){
        try{
            if (!Objects.equals(id, Long.parseLong(idFromAccessToken))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            updateUserUseCase.updateUser(id, UserRequestConverter.convertUpdateRequest(request));
            return ResponseEntity.ok().build();

        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") final long id,
                                           @RequestHeader("X-User-Id") String idFromAccessToken){
        try{
            if (!Objects.equals(id, Long.parseLong(idFromAccessToken))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            deactivateUserUseCase.deactivateUser(id);
            return ResponseEntity.ok().build();

        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
