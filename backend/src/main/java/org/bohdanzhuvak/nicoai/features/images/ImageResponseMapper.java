package org.bohdanzhuvak.nicoai.features.images;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponseSimplified;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.service.FileService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageResponseMapper {

  private final FileService fileService;

  public ImageResponse toImageResponse(Image image, boolean isLiked) {
    byte[] images = fileService.readFileBytes(image.getImageData().getName());
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

  public ImageResponseSimplified toImageResponseSimplified(Image image, boolean isLiked) {
    return ImageResponseSimplified.builder()
        .id(image.getId())
        .isLiked(isLiked)
        .countLikes(image.getLikeCount())
        .build();
  }
}