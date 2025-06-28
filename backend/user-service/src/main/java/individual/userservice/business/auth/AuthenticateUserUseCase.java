package individual.userservice.business.auth;

public interface AuthenticateUserUseCase {
    String authenticateUser(String email, String password);
}
