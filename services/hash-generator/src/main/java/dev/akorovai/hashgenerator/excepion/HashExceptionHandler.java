package dev.akorovai.hashgenerator.excepion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class HashExceptionHandler {

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
		log.error("Post not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(HashGenerationException.class)
	public ResponseEntity<String> handleHashGenerationException(HashGenerationException ex) {
		log.error("Hash generation error: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
