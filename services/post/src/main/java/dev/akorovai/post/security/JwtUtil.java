package dev.akorovai.post.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	private final SecretKey key;

	public JwtUtil(@Value("${application.security.jwt.secret-key}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public String extractUserId(String token) {
		Claims claims = Jwts.parserBuilder()
				                .setSigningKey(key)
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
