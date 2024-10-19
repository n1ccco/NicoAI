package org.bohdanzhuvak.nicoai.features.images;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.request.PromptRequest;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.ImageData;
import org.bohdanzhuvak.nicoai.features.images.model.PromptData;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.config.ImageProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageFactory {

  private final ImageProperties imageProperties;

  public Image createImage(String fileName, PromptRequest promptRequest, User author) {
    ImageData imageData = ImageData.builder()
        .name(fileName)
        .type("image/png")
        .path(imageProperties.getFOLDER_PATH() + fileName)
        .build();

    PromptData promptData = PromptData.builder()
        .prompt(promptRequest.getPrompt())
        .negativePrompt(promptRequest.getNegativePrompt())
        .height(promptRequest.getHeight())
        .width(promptRequest.getWidth())
        .numInterferenceSteps(promptRequest.getNumInterferenceSteps())
        .guidanceScale(promptRequest.getGuidanceScale())
        .build();

    return Image.builder()
        .author(author)
        .imageData(imageData)
        .promptData(promptData)
        .build();
  }
}