package org.bohdanzhuvak.nicoai.images;

import org.bohdanzhuvak.nicoai.features.images.dto.request.PromptRequest;
import org.bohdanzhuvak.nicoai.features.images.dto.response.GenerateResponse;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.images.service.ImageGeneratorService;
import org.bohdanzhuvak.nicoai.features.images.service.ImageService;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.bohdanzhuvak.nicoai.shared.config.ImageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/db/test-data/ImageServiceTest-data.sql")
public class ImageServiceTest {

  @TempDir
  Path tempDir;
  @MockBean
  private ImageGeneratorService imageGeneratorService;
  @Autowired
  private ImageRepository imageRepository;
  @Autowired
  private ImageService imageService;
  @Autowired
  private ImageProperties imageProperties;
  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    imageProperties.setFOLDER_PATH(tempDir.toString());
    user = userRepository.findByUsername("user").orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Test
  public void testGenerateImage() {

    byte[] imageBytes = { /* image data */};

    // Mock the image generator to return image bytes
    when(imageGeneratorService.fetchImageFromGenerator(any(PromptRequest.class)))
        .thenReturn(imageBytes);

    // Mock MultipartFile and simulate file saving
    MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
    when(mockMultipartFile.getName()).thenReturn("mockImage.png");

    // Act
    PromptRequest promptRequest = new PromptRequest("image prompt", null, 512, 512, 25, 5);
    GenerateResponse response = imageService.generateImage(promptRequest, user);

    // Assert
    assertNotNull(response.getImageId());

    Optional<Image> savedImage = imageRepository.findById(response.getImageId());
    assertTrue(savedImage.isPresent());
    assertNotNull(savedImage.get().getImageData().getPath());
  }
}