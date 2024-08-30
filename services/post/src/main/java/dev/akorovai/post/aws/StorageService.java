package dev.akorovai.post.aws;

import dev.akorovai.post.config.AwsS3BucketProperties;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3BucketProperties.class)
public class StorageService {

	private final S3Template s3Template;
	private final AwsS3BucketProperties awsS3BucketProperties;

	public void save(MultipartFile file) throws IOException {
		String objectKey = getObjectKey(file);
		var bucketName = awsS3BucketProperties.getBucketName();
		try (var inputStream = file.getInputStream()) {
			s3Template.upload(bucketName, objectKey, inputStream);
		}
	}

	public S3Resource retrieve(String objectKey) {
		Objects.requireNonNull(objectKey, "Object key must not be null");
		var bucketName = awsS3BucketProperties.getBucketName();
		return s3Template.download(bucketName, objectKey);
	}

	public void delete(String objectKey) {
		Objects.requireNonNull(objectKey, "Object key must not be null");
		var bucketName = awsS3BucketProperties.getBucketName();
		s3Template.deleteObject(bucketName, objectKey);
	}

	private String getObjectKey(MultipartFile file) {
		String objectKey = file.getOriginalFilename();
		return Objects.requireNonNull(objectKey, "File name must not be null");
	}
}