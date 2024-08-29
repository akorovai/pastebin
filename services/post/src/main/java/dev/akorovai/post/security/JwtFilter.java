package dev.akorovai.post.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

	private final SecretKey key;

	public JwtFilter(@Value("${application.security.jwt.secret-key}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		log.debug("JWT secret key initialized successfully.");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null) {
			handleUnauthorized(response, "Authorization header is missing");
			return;
		}

		if (!authHeader.startsWith("Bearer ")) {
			handleUnauthorized(response, "Invalid Authorization header format. Expected 'Bearer <token>', received: {}", authHeader);
			return;
		}

		final String token = authHeader.substring(7);
		if (token.isEmpty()) {
			handleUnauthorized(response, "JWT token is missing after 'Bearer '");
			return;
		}

		log.debug("Processing JWT token");

		try {
			Claims claims = Jwts.parserBuilder()
					                .setSigningKey(key)
					                .build()
					                .parseClaimsJws(token)
					                .getBody();

			if (claims.getSubject() == null) {
				throw new JwtException("JWT token is missing 'subject' claim");
			}

			request.setAttribute("claims", claims);
			log.debug("JWT token successfully parsed with claims: {}", claims);

		} catch (JwtException e) {
			log.error("JWT validation error: {}", e.getMessage());
			handleUnauthorized(response, "Invalid or expired JWT token: {}", e.getMessage());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void handleUnauthorized(HttpServletResponse response, String message, Object... args) throws IOException {
		log.warn(message, args);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(String.format(message, args));
	}
}
