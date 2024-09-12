package org.bohdanzhuvak.nicoai.service;

import org.bohdanzhuvak.nicoai.NicoAiApplication;
import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NicoAiApplication.class)
@ActiveProfiles("test")
public class ImageServiceTest {

  @MockBean
  private FileService fileService;  // Still mock external service

  @MockBean
  private RestTemplate restTemplate;  // Still mock external service

  @MockBean
  private ImageGeneratorService imageGeneratorService;  // Still mock external service

  @Autowired
  private ImageRepository imageRepository;  // Use actual repository

  @Autowired
  private ImageService imageService;  // Use actual service

  @Autowired
  private ImageProperties imageProperties;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    imageProperties.setFOLDER_PATH(tempDir.toString());
    imageRepository.deleteAll();  // Clean up the test database before each test
  }

  @Test
  public void testGenerateImage() throws Exception {
    // Arrange
    User user = new User();
    user.setId(1L);
    user.setUsername("username");

    byte[] imageBytes = { /* image data */ };

    // Mock the image generator to return image bytes
    when(imageGeneratorService.fetchImageFromGenerator(any(PromptRequest.class)))
        .thenReturn(imageBytes);

    // Mock MultipartFile and simulate file saving
    MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
    when(mockMultipartFile.getName()).thenReturn("mockImage.png");
    when(fileService.saveImageToFileSystem(any(byte[].class), anyString()))
        .thenReturn(mockMultipartFile);

    // Act
    PromptRequest promptRequest = new PromptRequest("image prompt", null, 512, 512, 25, 5);
    GenerateResponse response = imageService.generateImage(promptRequest, user);

    // Assert
    assertNotNull(response.getImageId());

    // Verify that the image has been saved in the test database
    Optional<Image> savedImage = imageRepository.findById(response.getImageId());
    assertTrue(savedImage.isPresent());  // This should now return true using the actual database
    assertEquals("mockImage.png", savedImage.get().getImageData().getName());  // Check that the image data is correct
  }

  @Test
  public void testGenerateImage_SavingFailed() throws Exception {
    // Arrange
    User user = new User();
    user.setId(1L);
    user.setUsername("username");

    byte[] imageBytes = { /* image data */ };

    // Mock the image generator to return image bytes
    when(imageGeneratorService.fetchImageFromGenerator(any(PromptRequest.class)))
        .thenReturn(imageBytes);

    // Simulate failure in saving the image to the file system
    doThrow(new IOException("Mocked IOException"))
        .when(fileService).saveImageToFileSystem(any(byte[].class), anyString());

    // Act
    PromptRequest promptRequest = new PromptRequest("image prompt", null, 512, 512, 25, 5);
    GenerateResponse response = imageService.generateImage(promptRequest, user);

    // Assert
    assertNotNull(response);
    assertNull(response.getImageId());  // Expecting null since saving failed
  }
}