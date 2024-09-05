package dev.akorovai.authentication.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<ResponseObject> register(@RequestBody @Valid RegistrationRequest request) {
		log.info("Registering user with nickname: {}", request.getNickname());
		try {
			service.register(request);
			ResponseObject responseObject = ResponseObject.builder()
					                                .code(HttpStatus.CREATED.value())
					                                .message("User registered successfully")
					                                .build();
			log.info("User registered successfully: {}", request.getNickname());
			return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
		} catch (Exception e) {
			log.error("Error registering user: {}", request.getNickname(), e);
			ResponseObject responseObject = ResponseObject.builder()
					                                .code(HttpStatus.BAD_REQUEST.value())
					                                .message("Invalid input")
					                                .build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request) {
		log.info("Authenticating user with nickname: {}", request.getNickname());
		try {
			AuthenticationResponse authResponse = service.authenticate(request);
			ResponseObject responseObject = ResponseObject.builder()
					                                .code(HttpStatus.OK.value())
					                                .message("Authentication successful. Token: " + authResponse.getToken())
					                                .build();
			log.info("User authenticated successfully: {}", request.getNickname());
			return ResponseEntity.ok(responseObject);
		} catch (Exception e) {
			log.error("Error authenticating user: {}", request.getNickname(), e);
			ResponseObject responseObject = ResponseObject.builder()
					                                .code(HttpStatus.UNAUTHORIZED.value())
					                                .message("Unauthorized")
					                                .build();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseObject);
		}
	}


}