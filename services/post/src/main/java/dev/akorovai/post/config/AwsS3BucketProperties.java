package dev.akorovai.post.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.s3")
public class AwsS3BucketProperties {
	@NotBlank(message = "S3 bucket name must be configured")
	private String bucketName;
}