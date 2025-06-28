package individual.userservice.configuration.security.token;


public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRole();

    boolean hasRole(String roleName);
}
