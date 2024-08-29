package dev.akorovai.authentication.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseObject {
	private int code;
	private String message;
}