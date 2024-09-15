package com.likelion.likelionoauthlogin.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    //amazon s3 클라이언트 주입
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // multipart file -> file 로 전환: s3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)    // multipartfile -> file 변환하는 메서드 작성
                .orElseThrow( () -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        return upload(uploadFile, dirName);
    }

    // file을 s3에 업로드
    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" +uploadFile.getName(); // 디렉토리이름/파일이름
        // 파일을 s3에 업로드하고 url 반환
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 변환 과정(convert)에서 생성된 로컬 파일 삭제하는 로직 짜기
        removeNewFile(uploadFile);

        return uploadImageUrl;

    }

    public String putS3(File uploadFile, String fileName) {
        // s3 버킷에다가 파일 업로드 하는 로직
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile) //파일 업로드
        );
        return amazonS3.getUrl(bucket, fileName).toString(); // 업로드 된 파일 url 반환
    }

    public void removeNewFile(File targetFile) {
        String name = targetFile.getName();

        if (targetFile.delete()) {
            log.info(name + "파일 삭제 완료");
        } else {
            log.info(name + "파일 삭제 실패");
        }
    }

    public Optional<File> convert(MultipartFile multipartFile) throws IOException {
        // 파일 이름 보관
        File convertFile = new File(multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
