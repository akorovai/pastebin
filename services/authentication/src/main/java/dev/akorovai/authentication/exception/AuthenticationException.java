package dev.akorovai.authentication.exception;

public class AuthenticationException extends RuntimeException {


    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}