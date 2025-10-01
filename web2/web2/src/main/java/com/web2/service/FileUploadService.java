package com.web2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + uniqueFilename;
    }

    public void deleteFile(String filename) {
        if (filename != null && !filename.isEmpty()) {
            try {
                String cleanFilename = filename.startsWith("/uploads/") 
                    ? filename.substring(9) 
                    : filename;
                
                Path filePath = Paths.get(uploadDir, cleanFilename);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Erro ao deletar arquivo: " + e.getMessage());
            }
        }
    }
}
