package dev.akorovai.authentication.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@Builder
public class RegistrationRequest {


	@NotEmpty(message = "Nickname is mandatory")
	@NotBlank(message = "Nickname is mandatory")
	private String nickname;
	@NotEmpty(message = "Password is mandatory")
	@NotBlank(message = "Nickname is mandatory")
	@Size(min = 8, message = "Password should be 8 characters long minimum")
	private String password;
}