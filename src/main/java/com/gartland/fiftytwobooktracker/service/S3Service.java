package com.gartland.fiftytwobooktracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service for managing file uploads to AWS S3 using Path.
 */
@Service
public class S3Service {

    private final String bucketName;
    private final S3Client s3Client;

    /**
     * Constructs the S3Service with the specified configuration.
     *
     * @param bucketName The S3 bucket name.
     * @param region     The AWS region.
     * @param accessKey  The AWS access key.
     * @param secretKey  The AWS secret key.
     */
    public S3Service(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.region}") String region,
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey) {

        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                ).build();
    }

    /**
     * Uploads a file to S3 using Path.
     *
     * @param file The MultipartFile to upload.
     * @return The URL of the uploaded file.
     */
    public String uploadFile(MultipartFile file) {
        String key = "images/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path tempFile;

        try {
            // Create a temporary file
            tempFile = Files.createTempFile(UUID.randomUUID().toString(), file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    tempFile
            );

        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
