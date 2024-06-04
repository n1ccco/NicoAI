package org.bohdanzhuvak.nicoai.dto;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomMultipartFile implements MultipartFile {
  private final String name;

  private final String originalFilename;

  private final String contentType;

  private final byte[] content;

  public CustomMultipartFile(String name, byte[] content) {
    this(name, "", "image/png", content);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getOriginalFilename() {
    return originalFilename;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public boolean isEmpty() {
    return content == null || content.length == 0;
  }

  @Override
  public long getSize() {
    return content.length;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return content;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  @Override
  public void transferTo(File destination) throws IOException, IllegalStateException {
    try (FileOutputStream fos = new FileOutputStream(destination)) {
      fos.write(content);
    }
  }
}