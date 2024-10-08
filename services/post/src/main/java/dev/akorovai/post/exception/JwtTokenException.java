package dev.akorovai.post.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class JwtTokenException extends RuntimeException {
    private final String message;

}
