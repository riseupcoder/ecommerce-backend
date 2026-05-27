package org.god.godecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        Path uploadDir = Paths.get(path);
        Files.createDirectories(uploadDir);

        String fileName = UUID.randomUUID() + "_" +
                Objects.requireNonNull(
                                image.getOriginalFilename())
                        .substring(image.getOriginalFilename().lastIndexOf("."));

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
