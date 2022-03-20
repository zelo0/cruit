package com.project.cruit.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class ImageResizeService {
    public ByteArrayInputStream resizeImage(MultipartFile file, int targetWidth, int targetHeight) throws IOException {
        // image ratio 유지
        BufferedImage output = Thumbnails.of(file.getInputStream()).size(targetWidth, targetHeight).asBufferedImage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(output, "png", outputStream);
        log.info("file's original size: {}, after resized size: {}", file.getSize(), outputStream.size());
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
