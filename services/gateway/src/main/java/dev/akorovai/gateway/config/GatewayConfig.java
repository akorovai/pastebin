package dev.akorovai.gateway.config;

import dev.akorovai.gateway.post.PostRequest;
import dev.akorovai.gateway.post.PostRequestModifier;
import dev.akorovai.gateway.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r
                .path("/api/auth/**")
                .uri("lb://AUTH-SERVICE"))
            .route("post-create", r -> r
                .path("/api/posts")
                .and()
                .method("POST")
                .and()
                .readBody(PostRequest.class, body -> true)
                .filters(f -> f
                    .filter(jwtFilter.apply(new JwtFilter.Config()))
                    .modifyRequestBody(
                        PostRequest.class,
                        PostRequest.class,
                        MediaType.APPLICATION_JSON_VALUE,
                        new PostRequestModifier()
                    ))
                .uri("lb://POST-SERVICE"))
            .route("post-service-other", r -> r
                .path("/api/posts/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                .uri("lb://POST-SERVICE"))
            .build();
    }
}
