package org.bohdanzhuvak.nicoai.service.mapper;

import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.service.FileService;
import org.bohdanzhuvak.nicoai.service.InteractionService;
import org.springframework.stereotype.Component;

@Component
public class ImageResponseMapper {

  private final FileService fileService;
  private final InteractionService interactionService;

  public ImageResponseMapper(FileService fileService, InteractionService interactionService) {
    this.fileService = fileService;
    this.interactionService = interactionService;
  }

  public ImageResponse toImageResponse(Image image, Long userId) {
    byte[] images = fileService.readFileBytes(image.getImageData().getName());
    boolean isLiked = userId != null && interactionService.checkIfUserLikedImage(image, userId);
    int countLikes = image.getLikes().size();

    return ImageResponse.builder()
        .id(image.getId())
        .promptData(image.getPromptData())
        .authorId(image.getAuthor().getId())
        .authorName(image.getAuthor().getUsername())
        .isPublic(image.isPublic())
        .isLiked(isLiked)
        .countLikes(countLikes)
        .imageData(images)
        .build();
  }
}