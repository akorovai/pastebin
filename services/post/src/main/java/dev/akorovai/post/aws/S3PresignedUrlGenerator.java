package dev.akorovai.post.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
public class S3PresignedUrlGenerator {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    public String generatePresignedUrl( String bucketName, String objectKey ) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3Presigner presigner = S3Presigner.builder()
                                        .region(Region.of(region))
                                        .credentialsProvider(StaticCredentialsProvider.create(credentials))
                                        .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(objectKey)
                                                    .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                                                                  .signatureDuration(Duration.ofMinutes(5))
                                                                  .getObjectRequest(getObjectRequest)
                                                                  .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        String presignedUrl = presignedGetObjectRequest.url().toString();

        presigner.close();

        return presignedUrl;
    }
}