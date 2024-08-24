package dev.akorovai.authentication.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

	@NotEmpty(message = "Nikcname is mandatory")
	@NotNull(message = "Nikcname is mandatory")
	private String nickname;

	@NotEmpty(message = "Password is mandatory")
	@NotNull(message = "Password is mandatory")
	@Size(min = 8, message = "Password should be 8 characters long minimum")
	private String password;
}