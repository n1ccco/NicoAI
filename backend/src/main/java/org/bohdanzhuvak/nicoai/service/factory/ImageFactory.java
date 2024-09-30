package org.bohdanzhuvak.nicoai.service.factory;

import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.ImageData;
import org.bohdanzhuvak.nicoai.model.PromptData;
import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.stereotype.Component;

@Component
public class ImageFactory {

  private final ImageProperties imageProperties;

  public ImageFactory(ImageProperties imageProperties) {
    this.imageProperties = imageProperties;
  }

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