package dev.akorovai.authentication.auth;

import dev.akorovai.authentication.handler.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	@Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User registered successfully",
					content = @Content(schema = @Schema(implementation = ResponseObject.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input",
					content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	})
	public ResponseEntity<ResponseObject> register(@RequestBody @Valid RegistrationRequest request) {
		service.register(request);
		ResponseObject responseObject = ResponseObject.builder()
				                                .code(HttpStatus.CREATED.value())
				                                .message("User registered successfully")
				                                .build();
		return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
	}

	@PostMapping("/login")
	@Operation(summary = "Authenticate a user", description = "Authenticates a user with the provided credentials.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Authentication successful",
					content = @Content(schema = @Schema(implementation = ResponseObject.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
					content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	})
	public ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request) {
		AuthenticationResponse authResponse = service.authenticate(request);
		ResponseObject responseObject = ResponseObject.builder()
				                                .code(HttpStatus.OK.value())
				                                .message("Authentication successful. Token: " + authResponse.getToken())
				                                .build();
		return ResponseEntity.ok(responseObject);
	}
}