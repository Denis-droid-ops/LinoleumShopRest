package com.kuznetsov.linoleumShopRest.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@Slf4j
public class ImageService {

    @Value("${app.spring.bucket:/Users/vilu/IdeaProjects/LinoleumShopRest/image}")
    private String bucket;

    @SneakyThrows
    @Transactional
    public void upload(String imagePath, InputStream image) {
        log.info("Start image uploading, imagePath is {}",imagePath);
        Path imageFullPath = Path.of(bucket,imagePath);
        try (image){
            Files.createDirectories(imageFullPath.getParent());
            Files.write(imageFullPath,image.readAllBytes()
                    ,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath) {
        log.info("Start image getting, imagePath is {}",imagePath);
        Path imageFullPath = Path.of(bucket,imagePath);
        if(Files.exists(imageFullPath)){
            return Optional.of(Files.readAllBytes(imageFullPath));
        }else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    @Transactional
    public void delete(String imagePath) {
        log.info("Start image deleting, imagePath is {}",imagePath);
        Path imageFullPath = Path.of(bucket,imagePath);
        if(Files.exists(imageFullPath)){
            Files.delete(imageFullPath);
        }
    }


}
