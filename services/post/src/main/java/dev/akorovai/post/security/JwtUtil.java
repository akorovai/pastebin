package dev.akorovai.post.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	public String extractUserId(String token) {
		Claims claims = Jwts.parserBuilder()
				                .setSigningKey(secretKey.getBytes())
				                .build()
				                .parseClaimsJws(stripTokenPrefix(token))
				                .getBody();

		return claims.get("userId", String.class);
	}

	private String stripTokenPrefix(String token) {
		if (token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return token;
	}
}
