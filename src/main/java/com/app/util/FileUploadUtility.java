package com.app.util;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
 
@Component
public class FileUploadUtility {
 
    private static final String BASE_FOLDER = "C://alcohol";
    private static final String BASE_URL = "http://localhost:8080/alcohol/";
 
    // Upload new file
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            String folderPath = BASE_FOLDER + "//" + folderName;
            Files.createDirectories(Paths.get(folderPath));
 
            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;
            String fullPath = folderPath + "//" + uniqueFileName;
 
            file.transferTo(new File(fullPath));
 
            return BASE_URL + folderName + "/" + uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
 
    // Replace existing file if any
    public String replaceFile(MultipartFile newFile, String folderName, String oldFileUrl) {
        if (oldFileUrl != null && !oldFileUrl.isBlank()) {
            deleteFile(oldFileUrl);
        }
        return uploadFile(newFile, folderName);
    }
 
    // Delete file by URL
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || !fileUrl.contains(BASE_URL)) return;
 
            String relativePath = fileUrl.replace(BASE_URL, "").replace("/", "//");
            String fullPath = BASE_FOLDER + "//" + relativePath;
            Files.deleteIfExists(Paths.get(fullPath));
        } catch (IOException e) {
            System.err.println("File delete failed: " + e.getMessage());
        }
    }
}
 