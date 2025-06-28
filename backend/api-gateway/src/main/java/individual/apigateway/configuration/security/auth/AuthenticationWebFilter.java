package individual.apigateway.configuration.security.auth;

import individual.apigateway.configuration.security.token.AccessToken;
import individual.apigateway.configuration.security.token.AccessTokenDecoder;
import individual.apigateway.configuration.security.token.exception.InvalidAccessTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthenticationWebFilter implements WebFilter {

    private static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";
    private final AccessTokenDecoder accessTokenDecoder;

    @Autowired
    public AuthenticationWebFilter(AccessTokenDecoder accessTokenDecoder) {
        this.accessTokenDecoder = accessTokenDecoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        try {
            AccessToken accessToken = accessTokenDecoder.decode(token);

            exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", String.valueOf(accessToken.getUserId()))
                    .header("X-Role", accessToken.getRole())
                    .header("X-Email", accessToken.getSubject())
                    .build();

            UserDetails userDetails = new User(
                    accessToken.getSubject(),
                    "",
                    Collections.singletonList(
                            new SimpleGrantedAuthority(SPRING_SECURITY_ROLE_PREFIX + accessToken.getRole()))
            );

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        } catch (InvalidAccessTokenException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
