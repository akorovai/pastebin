package dev.akorovai.authentication.auth;

import dev.akorovai.authentication.role.RoleRepository;
import dev.akorovai.authentication.security.JwtService;
import dev.akorovai.authentication.user.User;
import dev.akorovai.authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;



	public void register(RegistrationRequest request) {
		var userRole = roleRepository.findByName("USER")
				               .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
		var user = User.builder()
				           .nickname(request.getNickname())
				           .password(passwordEncoder.encode(request.getPassword()))
				           .roles(List.of(userRole))
				           .build();
		userRepository.save(user);
	}


	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getNickname(), request.getPassword())
		);

		var claims = new HashMap<String, Object>();
		var user = (User) auth.getPrincipal();
		claims.put("nickname", user.getNickname());

		var jwtToken = jwtService.generateToken(claims, user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
}
