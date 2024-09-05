package dev.akorovai.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

	private final SecretKey key;

	public JwtFilter(@Value("${application.security.jwt.secret-key}") String secretKey) {
		super(Config.class);
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String authHeader = request.getHeaders().getFirst("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return onError(exchange, "Authorization header is missing or invalid");
			}

			String token = authHeader.substring(7);
			try {
				Claims claims = Jwts.parserBuilder()
						                .setSigningKey(key)
						                .build()
						                .parseClaimsJws(token)
						                .getBody();

				String userId = claims.get("userId", String.class);
				log.debug("Before adding X-User-Id header with value: {}", userId);
				ServerHttpRequest modifiedRequest = request.mutate()
						                                    .header("X-User-Id", userId)
						                                    .build();
				log.debug("After adding X-User-Id header with value: {}", userId);

				return chain.filter(exchange.mutate().request(modifiedRequest).build());
			} catch (JwtException e) {
				log.error("JWT validation error: {}", e.getMessage());
				return onError(exchange, "Invalid or expired JWT token");
			}
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		response.getHeaders().add("Content-Type", "application/json");
		return response.writeWith(Mono.just(buffer));
	}

	public static class Config {
	}
}