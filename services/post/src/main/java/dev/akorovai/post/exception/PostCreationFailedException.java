package dev.akorovai.post.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostCreationFailedException extends RuntimeException {
    private final String message;
}