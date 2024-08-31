package dev.akorovai.hashgenerator.excepion;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostNotActiveException extends RuntimeException {
	private final String message;
}
