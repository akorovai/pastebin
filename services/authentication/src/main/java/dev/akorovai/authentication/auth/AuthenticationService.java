package dev.akorovai.authentication.auth;


import dev.akorovai.authentication.user.User;

public interface AuthenticationService {

	void register(RegistrationRequest request);

	AuthenticationResponse authenticate(AuthenticationRequest request);



	String generateActivationCode(int length);
}
