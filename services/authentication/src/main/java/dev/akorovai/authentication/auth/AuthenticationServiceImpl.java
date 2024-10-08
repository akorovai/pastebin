package dev.akorovai.authentication.auth;

import dev.akorovai.authentication.exception.AuthenticationException;
import dev.akorovai.authentication.exception.RegistrationException;
import dev.akorovai.authentication.redis.UserCache;
import dev.akorovai.authentication.redis.UserCacheService;
import dev.akorovai.authentication.role.RoleRepository;
import dev.akorovai.authentication.security.JwtService;
import dev.akorovai.authentication.user.User;
import dev.akorovai.authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	private final UserCacheService userCacheService;
	private final ModelMapper modelMapper;

	@Override
	@Transactional
	public void register(RegistrationRequest request) {
		try {
			var userRole = roleRepository.findByName("USER")
					               .orElseThrow(() -> new RegistrationException("ROLE USER was not initiated"));

			var user = User.builder()
					           .nickname(request.getNickname())
					           .password(passwordEncoder.encode(request.getPassword()))
					           .roles(List.of(userRole))
					           .build();

			userRepository.save(user);
			cacheUser(user);
			log.info("User registered: {}", user.getNickname());
		} catch (Exception e) {
			log.error("Error registering user: {}", request.getNickname(), e);
			throw new RegistrationException("Error registering user: " + request.getNickname(), e);
		}
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		try {
			User user = authenticateAndGetUser(request);
			String jwtToken = generateToken(user);
			log.info("JWT token generated for user: {}", user.getNickname());
			return AuthenticationResponse.builder().token(jwtToken).build();
		} catch (Exception e) {
			log.error("Error authenticating user: {}", request.getNickname(), e);
			throw new AuthenticationException("Error authenticating user: " + request.getNickname(), e);
		}
	}

	private User authenticateAndGetUser(AuthenticationRequest request) {
		UserCache cachedUser = userCacheService.getUser(request.getNickname());
		log.debug("User cache check for nickname: {}", request.getNickname());

		if (cachedUser == null) {
			log.debug("User not found in cache, performing authentication");
			var auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getNickname(), request.getPassword()));
			User user = (User) auth.getPrincipal();
			cacheUser(user);
			log.info("User authenticated and cached: {}", user.getNickname());
			return user;
		} else {
			log.debug("User found in cache, verifying password");
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getNickname(), request.getPassword()));
			log.info("User authenticated from cache: {}", cachedUser.getNickname());
			return modelMapper.map(cachedUser, User.class);
		}
	}

	private void cacheUser(User user) {
		UserCache userCache = modelMapper.map(user, UserCache.class);
		userCacheService.cacheUser(userCache);
	}

	private String generateToken(User user) {
		var claims = new HashMap<String, Object>();
		claims.put("nickname", user.getNickname());
		return jwtService.generateToken(claims, user);
	}
}