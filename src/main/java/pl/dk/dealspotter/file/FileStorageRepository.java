package pl.dk.dealspotter.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class FileStorageRepository {

    @Value("${app.storage.location}")
    private String storageFolder;

    public void save(String originalFilename, InputStream inputStream) {
        try {
            Path filePatch = Path.of(storageFolder).resolve(originalFilename).normalize();
            Files.copy(inputStream, filePatch);
        } catch (IOException e) {
            originalFilename = null;
        }
    }


}
