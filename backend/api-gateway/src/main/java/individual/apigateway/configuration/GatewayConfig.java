package individual.apigateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.cloud.gateway.route.RouteLocator;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://charity-fundraising-frontend.azurewebsites.net");
        config.addAllowedOrigin("http://charity-fundraising-frontend.azurewebsites.net");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter());
                            c.setKeyResolver(ipKeyResolver());
                        }))
                        .uri("lb://user-service"))

                .route("charity-service", r -> r.path("/charities/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter());
                            c.setKeyResolver(ipKeyResolver());
                        }))
                        .uri("lb://charity-service"))

                .route("donation-service", r -> r.path("/donations/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter());
                            c.setKeyResolver(ipKeyResolver());
                        }))
                        .uri("lb://donation-service"))

                .route("payment-service", r -> r.path("/payments/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter());
                            c.setKeyResolver(ipKeyResolver());
                        }))
                        .uri("lb://donation-service"))

                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }
}
