package individual.apigateway.configuration.security;
import individual.apigateway.configuration.security.auth.AuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AuthenticationWebFilter authenticationWebFilter;

    @Autowired
    public SecurityConfig(AuthenticationWebFilter authenticationWebFilter) {
        this.authenticationWebFilter = authenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {})
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/prometheus").permitAll()
                        .pathMatchers(HttpMethod.GET, "/charities", "charities/{id}").permitAll()
                        .pathMatchers(HttpMethod.POST, "/users/auth").permitAll()
                        .pathMatchers(HttpMethod.GET, "/users", "/users/{id}").permitAll()
                        .pathMatchers(HttpMethod.POST, "/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/donations", "donations/charity/{id}").permitAll()
                        .pathMatchers(HttpMethod.POST, "/payments/complete").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

