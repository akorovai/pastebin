package dev.akorovai.post.security;

import dev.akorovai.post.exception.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtUtil {

	private final SecretKey key;

	public JwtUtil(@Value("${application.security.jwt.secret-key}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public String extractUserId(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					                .setSigningKey(key)
					                .build()
					                .parseClaimsJws(stripTokenPrefix(token))
					                .getBody();
			String userId = claims.get("userId", String.class);
			log.debug("Extracted userId: {}", userId);
			return userId;
		} catch (Exception e) {
			log.error("Failed to extract userId from token", e);
			throw new JwtTokenException("Invalid token");
		}
	}

	private String stripTokenPrefix(String token) {
		if (token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return token;
	}
}
