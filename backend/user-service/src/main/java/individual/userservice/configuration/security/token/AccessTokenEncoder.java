package individual.userservice.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
