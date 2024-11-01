package com.BSA.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AWSService {

	private final S3Client s3Client;
	private final String bucketName;

	public AWSService(@Value("${aws.accessKeyId}") String accessKeyId,
			@Value("${aws.secretAccessKey}") String secretAccessKey, @Value("${aws.s3.bucket}") String bucketName) {
		this.bucketName = bucketName;
		this.s3Client = S3Client.builder().region(Region.AP_SOUTH_1) // region
				.credentialsProvider(StaticCredentialsProvider //
						.create(AwsBasicCredentials // credentials
								.create(accessKeyId, secretAccessKey)))
				.build();

	}

	public String uploadFile(String filePath, String fileName) {
//		File file = new File(filePath);

		// Create a PutObjectRequest to specify bucket and file key in S3
		PutObjectRequest putObjectRequest = PutObjectRequest.builder() //
				.bucket(bucketName).key(fileName).build();

		// Uploads the file located at filePath to the specified bucket
		s3Client.putObject(putObjectRequest, Paths.get(filePath));

		// Generates a public URL for the uploaded file and returns it as a string
		return s3Client.utilities().getUrl(b -> b.bucket(bucketName) //
				.key(fileName)).toExternalForm();
	}

	public void downloadFileToLocal(String key) throws IOException {
		String localFilePath = "C:/Users/jayan/Documents/workspace-sts/BankStatementAggregator/BankStatements/Downloads/"
				+ key;

		// Create a GetObjectRequest specifying the bucket and the key of the file to be
		// downloaded
		GetObjectRequest getObjectRequest = GetObjectRequest.builder() //
				.bucket(bucketName).key(key).build();

		// Retrieve the file as an input stream from S3
		ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

		// Use FileUtils to save the file to the specified local path
		FileUtils.copyInputStreamToFile(s3Object, new File(localFilePath));
	}

}
