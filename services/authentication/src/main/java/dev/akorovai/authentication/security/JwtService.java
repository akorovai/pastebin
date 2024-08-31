package dev.akorovai.authentication.security;

import dev.akorovai.authentication.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	public String extractUsername(String token) {
		log.debug("Extracting username from token");
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		log.debug("Extracting claim from token");
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		log.debug("Generating token for user: {}", userDetails.getUsername());
		if (userDetails instanceof User user) {
			extraClaims.put("userId", user.getId().toString());
		}
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	private String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration
	) {
		log.debug("Building token for user: {}", userDetails.getUsername());
		var authorities = userDetails.getAuthorities()
				                  .stream()
				                  .map(GrantedAuthority::getAuthority)
				                  .toList();
		return Jwts
				       .builder()
				       .setClaims(extraClaims)
				       .setSubject(userDetails.getUsername())
				       .setIssuedAt(new Date(System.currentTimeMillis()))
				       .setExpiration(new Date(System.currentTimeMillis() + expiration))
				       .claim("authorities", authorities)
				       .signWith(getSignInKey())
				       .compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		log.debug("Validating token for user: {}", userDetails.getUsername());
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		log.debug("Checking if token is expired");
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		log.debug("Extracting expiration date from token");
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		log.debug("Extracting all claims from token");
		return Jwts
				       .parserBuilder()
				       .setSigningKey(getSignInKey())
				       .build()
				       .parseClaimsJws(token)
				       .getBody();
	}

	private Key getSignInKey() {
		log.debug("Getting signing key");
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}