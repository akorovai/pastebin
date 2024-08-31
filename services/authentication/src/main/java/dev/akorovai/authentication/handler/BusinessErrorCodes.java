package dev.akorovai.authentication.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum BusinessErrorCodes {
	NO_CODE(0, NOT_IMPLEMENTED, "No code"),
	INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
	NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
	ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
	ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
	BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
	AUTHENTICATION_FAILED(305, UNAUTHORIZED, "Authentication failed"),
	REGISTRATION_FAILED(306, BAD_REQUEST, "Registration failed"),
	GENERIC_ERROR(307, INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
	ACTIVATION_TOKEN_ERROR(308, BAD_REQUEST, "Activation token error"),
	OPERATION_NOT_PERMITTED(309, BAD_REQUEST, "Operation not permitted"),
	METHOD_ARGUMENT_NOT_VALID(310, BAD_REQUEST, "Method argument not valid");

	private final int code;
	private final HttpStatus httpStatus;
	private final String description;


}