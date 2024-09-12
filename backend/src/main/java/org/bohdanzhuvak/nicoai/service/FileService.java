package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.dto.CustomMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class FileService {
  private final ImageProperties imageProperties;

  protected MultipartFile saveImageToFileSystem(byte[] imageBytes, String filename) throws IOException {
    MultipartFile multipartFile = new CustomMultipartFile(filename, imageBytes);
    String filePath = imageProperties.getFOLDER_PATH() + multipartFile.getName();
    multipartFile.transferTo(new File(filePath));
    return multipartFile;
  }

  public byte[] readFileBytes(String fileName) throws IOException {
    return Files.readAllBytes(new File(imageProperties.getFOLDER_PATH() + fileName).toPath());
  }
}
