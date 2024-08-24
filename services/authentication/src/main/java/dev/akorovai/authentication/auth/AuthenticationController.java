package dev.akorovai.authentication.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	private final AuthenticationService service;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> register( @RequestBody @Valid RegistrationRequest request ) {
		service.register(request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate( @RequestBody AuthenticationRequest request ) {
		return ResponseEntity.ok(service.authenticate(request));
	}


}
