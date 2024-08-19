package org.bohdanzhuvak.nicoai.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.bohdanzhuvak.nicoai.NicoAiApplication;
import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
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

@SpringBootTest(classes = NicoAiApplication.class)
@ActiveProfiles("test")
public class ImageServiceTest {
  @Autowired
  private ImageService imageService;

  @Autowired
  private ImageRepository imageRepository;

  @MockBean
  private RestTemplate restTemplate;

  @Autowired
  private ImageProperties imageProperties;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    imageProperties.setFOLDER_PATH(tempDir.toString());
  }

  @Test
  public void testGenerateImage() {
    User user = new User();
    user.setId(1L);
    user.setUsername("nigger");
    byte[] imageBytes = { /* image data */ };
    when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(imageBytes);

    PromptRequest promptRequest = new PromptRequest("image prompt", null, 512, 512, 25, 5);
    GenerateResponse response = imageService.generateImage(promptRequest, user);

    assertNotNull(response.getImageId());
    assertTrue(imageRepository.findById(response.getImageId()).isPresent());
  }

  @Test
  public void testGenerateImage_SavingFailed() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("nigger");
    byte[] imageBytes = { /* image data */ };
    when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(imageBytes);
    ImageService spyImageService = Mockito.spy(imageService);
    doThrow(new IOException("Mocked IOException"))
        .when(spyImageService).saveImageToFileSystem(any(byte[].class), anyString());

    PromptRequest promptRequest = new PromptRequest("image prompt", null, 512, 512, 25, 5);
    GenerateResponse response = spyImageService.generateImage(promptRequest, user);

    assertNotNull(response);
    assertTrue(response instanceof GenerateResponse);
    assertTrue(response.getImageId() == null);
  }

  @Test
  void testChangeImage() {

  }

  @Test
  void testDeleteImage() {

  }

  @Test
  void testGetAllImages() {

  }

  @Test
  void testGetAllUserImages() {

  }

  @Test
  void testGetImage() {

  }

  @Test
  void testIsUserAuthenticated() {

  }
}
