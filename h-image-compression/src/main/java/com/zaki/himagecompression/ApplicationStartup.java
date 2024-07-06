package com.zaki.himagecompression;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String COMPRESSION_DIR = "compressionimage/";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            clearDirectory(UPLOAD_DIR);
            clearDirectory(COMPRESSION_DIR);
            System.out.println("Directories cleared successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearDirectory(String dir) throws IOException {
        Path directory = Paths.get(dir);
        if (Files.exists(directory)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                }
            }
        }
    }
}
