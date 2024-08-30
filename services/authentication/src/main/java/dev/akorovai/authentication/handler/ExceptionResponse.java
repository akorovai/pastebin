package dev.akorovai.authentication.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Exception response object")
public class ExceptionResponse {

	@Schema(description = "Business error code", example = "1001")
	private Integer businessErrorCode;

	@Schema(description = "Business error description", example = "Account locked")
	private String businessErrorDescription;

	@Schema(description = "Error message", example = "Invalid credentials")
	private String error;

	@Schema(description = "Validation errors", example = "[\"nickname must not be empty\", \"password must be at least 8 characters long\"]")
	private Set<String> validationErrors;

	@Schema(description = "Errors map", example = "{\"field1\": \"error message 1\", \"field2\": \"error message 2\"}")
	private Map<String, String> errors;
}