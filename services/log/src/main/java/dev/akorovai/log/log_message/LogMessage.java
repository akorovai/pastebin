package dev.akorovai.log.log_message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "log")
public class LogMessage {

	@Id
	private UUID id;

	@Field(type = FieldType.Text)
	@NotBlank(message = "Message cannot be blank")
	private String message;

	@Field(type = FieldType.Keyword)
	@NotBlank(message = "Type cannot be blank")
	private String type;

	@Field(type = FieldType.Date)
	@NotNull(message = "Timestamp cannot be null")
	private LocalDateTime timestamp;

	@Field(type = FieldType.Keyword)
	private String severity;

	@Field(type = FieldType.Text)
	private String source;

	public LogMessage() {
		this.id = UUID.randomUUID();
		this.timestamp = LocalDateTime.now();
	}

	public LogMessage(String message, String type, String severity, String source) {
		this.id = UUID.randomUUID();
		this.message = message;
		this.type = type;
		this.timestamp = LocalDateTime.now();
		this.severity = severity;
		this.source = source;
	}
}