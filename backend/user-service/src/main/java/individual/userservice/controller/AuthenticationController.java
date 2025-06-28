package individual.userservice.controller;

import individual.userservice.business.auth.AuthenticateUserUseCase;
import individual.userservice.business.auth.exception.InvalidCredentialsException;
import individual.userservice.controller.dto.auth.AuthenticateUserRequest;
import individual.userservice.controller.dto.auth.AuthenticateUserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticateUserUseCase authenticateUserUseCase;


    @PostMapping()
    public ResponseEntity<AuthenticateUserResponse> authenticateUser(@RequestBody @Valid AuthenticateUserRequest request) {
        try{
            AuthenticateUserResponse response = AuthenticateUserResponse.builder()
                    .accessToken(authenticateUserUseCase.authenticateUser(request.getEmail(), request.getPlainTextPassword()))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
