package dev.akorovai.post.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }

}
