package dev.akorovai.post.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostExceptionHandler {

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> handleFeignException(FeignException ex) {
		HttpStatus status = switch ( ex.status() ) {
			case 404 -> HttpStatus.NOT_FOUND;
			case 400 -> HttpStatus.BAD_REQUEST;
			default -> HttpStatus.INTERNAL_SERVER_ERROR;
		};
		return ResponseEntity.status(status).body(ex.getMessage());
	}
	@ExceptionHandler(JwtTokenException.class)
	public ResponseEntity<String> handleJwtTokenException(JwtTokenException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
}
