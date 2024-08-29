package dev.akorovai.authentication.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<ResponseObject> register(@RequestBody @Valid RegistrationRequest request) {
		service.register(request);
		// Create a response object for registration
		ResponseObject responseObject = ResponseObject.builder()
				                                .code(HttpStatus.CREATED.value())
				                                .message("User registered successfully")
				                                .build();
		return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request) {
		AuthenticationResponse authResponse = service.authenticate(request);

		ResponseObject responseObject = ResponseObject.builder()
				                                .code(HttpStatus.OK.value())
				                                .message("Authentication successful")
				                                .build();

		responseObject.setMessage("Authentication successful. Token: " + authResponse.getToken());

		return ResponseEntity.ok(responseObject);
	}
}
