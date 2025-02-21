package org.example.medinsurance.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads"; // Directory for storing images

    public FileStorageService() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeBase64Image(String base64Image) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            String filename = UUID.randomUUID().toString() + ".jpg";
            Path filePath = Paths.get(uploadDir, filename);
            Files.write(filePath, decodedBytes);
            return filename; // Return only the filename
        } catch (IOException e) {
            throw new RuntimeException("Failed to store Base64 image", e);
        }
    }

}
