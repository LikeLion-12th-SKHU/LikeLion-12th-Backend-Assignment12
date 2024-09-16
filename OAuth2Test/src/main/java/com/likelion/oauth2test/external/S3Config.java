package com.likelion.oauth2test.external;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.likelion.oauth2test.global.exception.BadRequestException;
import com.likelion.oauth2test.global.exception.NotFoundException;
import com.likelion.oauth2test.global.exception.model.Error;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
public class S3Config {

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public AmazonS3 s3Builder() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder
			.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			.build();
	}

}

