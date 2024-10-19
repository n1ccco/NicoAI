package org.bohdanzhuvak.nicoai.features.images;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponseSimplified;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.service.FileService;
import org.bohdanzhuvak.nicoai.features.images.service.InteractionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageResponseMapper {

  private final FileService fileService;
  private final InteractionService interactionService;

  public ImageResponse toImageResponse(Image image, Long userId) {
    byte[] images = fileService.readFileBytes(image.getImageData().getName());
    boolean isLiked = userId != null && interactionService.checkIfUserLikedImage(image, userId);

    return ImageResponse.builder()
        .id(image.getId())
        .promptData(image.getPromptData())
        .authorId(image.getAuthor().getId())
        .authorName(image.getAuthor().getUsername())
        .isPublic(image.getVisibility() == Visibility.PUBLIC)
        .isLiked(isLiked)
        .countLikes(image.getLikeCount())
        .imageData(images)
        .build();
  }

  public ImageResponseSimplified toImageResponseSimplified(Image image, Long userId) {
    boolean isLiked = userId != null && interactionService.checkIfUserLikedImage(image, userId);

    return ImageResponseSimplified.builder()
        .id(image.getId())
        .isLiked(isLiked)
        .countLikes(image.getLikeCount())
        .build();
  }
}