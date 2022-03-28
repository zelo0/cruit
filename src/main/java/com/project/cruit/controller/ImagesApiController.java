package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.UploadImageResponseDto;
import com.project.cruit.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImagesApiController {
    private final S3UploaderService s3UploaderService;

    @PostMapping("/api/v1/images")
    public ResponseWrapper<UploadImageResponseDto> uploadImageForPost(@RequestPart("file") MultipartFile file, @CurrentUser SessionUser sessionUser) throws IOException {
        SessionUser.checkIsNull(sessionUser);


        // s3에 업로드
        String fileUrl = s3UploaderService.upload(file, "posts");

        return new ResponseWrapper<>(new UploadImageResponseDto(fileUrl));
    }
}
