package com.project.cruit.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageResizeServiceTest {
    @InjectMocks
    private ImageResizeService imageResizeService;

    @Mock
    private MockMultipartFile multipartFile;

    @Test
    @DisplayName("이미지 사이즈 줄이기")
    void resizeImage() throws IOException {
        // given
        ClassPathResource resource = new ClassPathResource("/static/test-image.png");
        MultipartFile inputFile = new MockMultipartFile("test.jpg", resource.getInputStream());

        // when
        ByteArrayInputStream resizedStream = imageResizeService.resizeImage(inputFile, 50, 50);
        BufferedImage output = ImageIO.read(resizedStream);
        // 파일 만들어서 확인 (테스트)
        File outputFile = new File("resized.png");
        ImageIO.write(output, "png", outputFile);
        // then
        assertTrue(output.getWidth() == 50 || output.getHeight() == 50);
    }

}