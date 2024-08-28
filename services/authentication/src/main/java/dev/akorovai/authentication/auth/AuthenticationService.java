package dev.akorovai.authentication.auth;


public interface AuthenticationService {

	void register(RegistrationRequest request);

	AuthenticationResponse authenticate(AuthenticationRequest request);



}
