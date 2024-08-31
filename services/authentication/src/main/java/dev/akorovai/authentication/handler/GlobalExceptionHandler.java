package dev.akorovai.authentication.handler;

import dev.akorovai.authentication.exception.ActivationTokenException;
import dev.akorovai.authentication.exception.AuthenticationException;
import dev.akorovai.authentication.exception.OperationNotPermittedException;
import dev.akorovai.authentication.exception.RegistrationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static dev.akorovai.authentication.handler.BusinessErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity
				       .status(AUTHENTICATION_FAILED.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(AUTHENTICATION_FAILED.getCode())
								       .businessErrorDescription(AUTHENTICATION_FAILED.getDescription())
								       .error(e.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ExceptionResponse> handleRegistrationException(RegistrationException e) {
		return ResponseEntity
				       .status(REGISTRATION_FAILED.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(REGISTRATION_FAILED.getCode())
								       .businessErrorDescription(REGISTRATION_FAILED.getDescription())
								       .error(e.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
		return ResponseEntity
				       .status(ACCOUNT_LOCKED.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(ACCOUNT_LOCKED.getCode())
								       .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
								       .error(exp.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
		return ResponseEntity
				       .status(ACCOUNT_DISABLED.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(ACCOUNT_DISABLED.getCode())
								       .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
								       .error(exp.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleException() {
		return ResponseEntity
				       .status(BAD_CREDENTIALS.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(BAD_CREDENTIALS.getCode())
								       .businessErrorDescription(BAD_CREDENTIALS.getDescription())
								       .error("Login and / or Password is incorrect")
								       .build()
				       );
	}

	@ExceptionHandler(ActivationTokenException.class)
	public ResponseEntity<ExceptionResponse> handleException(ActivationTokenException exp) {
		return ResponseEntity
				       .status(ACTIVATION_TOKEN_ERROR.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(ACTIVATION_TOKEN_ERROR.getCode())
								       .businessErrorDescription(ACTIVATION_TOKEN_ERROR.getDescription())
								       .error(exp.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exp) {
		return ResponseEntity
				       .status(OPERATION_NOT_PERMITTED.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(OPERATION_NOT_PERMITTED.getCode())
								       .businessErrorDescription(OPERATION_NOT_PERMITTED.getDescription())
								       .error(exp.getMessage())
								       .build()
				       );
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
		Set<String> errors = new HashSet<>();
		exp.getBindingResult().getAllErrors()
				.forEach(error -> {
					var errorMessage = error.getDefaultMessage();
					errors.add(errorMessage);
				});

		return ResponseEntity
				       .status(METHOD_ARGUMENT_NOT_VALID.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(METHOD_ARGUMENT_NOT_VALID.getCode())
								       .businessErrorDescription(METHOD_ARGUMENT_NOT_VALID.getDescription())
								       .validationErrors(errors)
								       .build()
				       );
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
		exp.printStackTrace();
		return ResponseEntity
				       .status(GENERIC_ERROR.getHttpStatus())
				       .body(
						       ExceptionResponse.builder()
								       .businessErrorCode(GENERIC_ERROR.getCode())
								       .businessErrorDescription(GENERIC_ERROR.getDescription())
								       .error(exp.getMessage())
								       .build()
				       );
	}
}