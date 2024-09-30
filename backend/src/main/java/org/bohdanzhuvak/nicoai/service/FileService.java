package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.exception.FileStorageException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {
  private final ImageProperties imageProperties;

  public void saveImageToFileSystem(byte[] imageBytes, String filename) {
    try {
      Path folderPath = Paths.get(imageProperties.getFOLDER_PATH());
      Path filePath = folderPath.resolve(filename);
      Files.write(filePath, imageBytes);
    } catch (IOException e) {
      throw new FileStorageException("Failed to save image file: " + filename, e);
    }
  }

  public byte[] readFileBytes(String filename) {
    try {
      Path filePath = Paths.get(imageProperties.getFOLDER_PATH()).resolve(filename);
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      throw new FileStorageException("Failed to read image file: " + filename, e);
    }
  }
}
