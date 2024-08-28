package dev.akorovai.hashgenerator.excepion;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostNotFoundException extends RuntimeException {
	private final String message;
}