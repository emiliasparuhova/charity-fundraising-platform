package individual.apigateway.configuration.security.token;


public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRole();

    boolean hasRole(String roleName);
}
