package org.bohdanzhuvak.nicoai.controller;

import org.bohdanzhuvak.nicoai.dto.image.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.exception.ImageGenerationException;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ImageService imageService;

  @Test
  @WithMockUser(username = "user")
  public void testGenerateImage_ImageGenerationException() throws Exception {
    // Arrange
    PromptRequest promptRequest = new PromptRequest("car", "", 512, 512, 20, 8);
    when(imageService.generateImage(any(PromptRequest.class), any()))
        .thenThrow(new ImageGenerationException("Failed to generate image"));
    // Act & Assert
    mockMvc.perform(post("/api/images")
            .with(csrf())
            .params(promptRequest.toMultiValueMap()))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Image generation failed: Failed to generate image"));
  }

  @Test
  @WithMockUser(username = "user")
  public void testGenerateImage_Success() throws Exception {
    // Arrange
    PromptRequest promptRequest = new PromptRequest("car", "", 512, 512, 20, 8);

    GenerateResponse generateResponse = new GenerateResponse(100L);

    when(imageService.generateImage(any(PromptRequest.class), any()))
        .thenReturn(generateResponse);

    // Act & Assert
    mockMvc.perform(post("/api/images")
            .with(csrf())
            .params(promptRequest.toMultiValueMap()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imageId").value(100L));
  }
}