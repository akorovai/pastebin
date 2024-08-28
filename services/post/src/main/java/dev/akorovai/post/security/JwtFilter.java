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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtFilter extends GenericFilterBean {
	private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());
	private final SecretKey key;

	public JwtFilter(@Value("${application.security.jwt.secret-key}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;

		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authorization header is missing");
			return;
		}

		if (!authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid Authorization header format. Expected 'Bearer <token>'");
			return;
		}

		final String token = authHeader.substring(7);
		if (token.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("JWT token is missing after 'Bearer '");
			return;
		}

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

		} catch (JwtException e) {
			logger.severe("JWT validation error: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid or expired JWT token: " + e.getMessage());
			return;
		}


		filterChain.doFilter(request, response);
	}
}
