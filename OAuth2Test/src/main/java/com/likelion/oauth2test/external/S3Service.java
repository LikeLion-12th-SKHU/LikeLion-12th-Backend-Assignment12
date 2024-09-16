package com.likelion.oauth2test.external;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.likelion.oauth2test.global.exception.BadRequestException;
import com.likelion.oauth2test.global.exception.NotFoundException;
import com.likelion.oauth2test.global.exception.model.Error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadImage(MultipartFile multipartFile, String folder) {
		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try(InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket+"/"+ folder + "/image", fileName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
			return amazonS3.getUrl(bucket+"/"+ folder + "/image", fileName).toString();
		} catch(IOException e) {
			throw new NotFoundException(Error.IMAGE_NOT_FOUND_EXCEPTION, Error.IMAGE_NOT_FOUND_EXCEPTION.getMessage());
		}
	}

	// 파일명 (중복 방지)
	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	// 파일 유효성 검사
	private String getFileExtension(String fileName) {
		if (fileName.length() == 0) {
			throw new NotFoundException(Error.IMAGE_NOT_FOUND_EXCEPTION, Error.IMAGE_NOT_FOUND_EXCEPTION.getMessage());
		}
		ArrayList<String> fileValidate = new ArrayList<>();
		fileValidate.add(".jpg");
		fileValidate.add(".jpeg");
		fileValidate.add(".png");
		fileValidate.add(".JPG");
		fileValidate.add(".JPEG");
		fileValidate.add(".PNG");
		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!fileValidate.contains(idxFileName)) {
			throw new BadRequestException(Error.EXTENSION_NOT_VALID_ERROR, Error.EXTENSION_NOT_VALID_ERROR.getMessage());
		}
		return fileName.substring(fileName.lastIndexOf("."));
	}

	// 이미지 삭제
	public void deleteFile(String imageUrl) {
		String imageKey = imageUrl.substring(49);
		amazonS3.deleteObject(bucket, imageKey);
	}


}
