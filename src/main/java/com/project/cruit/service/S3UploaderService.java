package com.project.cruit.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.cruit.util.ImageResizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3UploaderService {
    private final AmazonS3Client amazonS3Client;
    private final ImageResizeService imageResizeService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dirName) throws IOException{
        String storedFilePath = dirName + "/" + UUID.randomUUID() + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        ByteArrayInputStream resizedStream = imageResizeService.resizeImage(file, 200, 200);
        amazonS3Client.putObject(bucket, storedFilePath, resizedStream, metadata);
        return amazonS3Client.getUrl(bucket, storedFilePath).toString();
    }
}
