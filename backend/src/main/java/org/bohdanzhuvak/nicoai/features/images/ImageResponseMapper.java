package org.bohdanzhuvak.nicoai.features.images;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponseSimplified;
import org.bohdanzhuvak.nicoai.features.images.model.BaseImage;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageResponseMapper {

  public ImageResponse toImageResponse(Image image, boolean isLiked) {
    return ImageResponse.builder()
        .id(image.getId())
        .authorId(image.getAuthor().getId())
        .authorName(image.getAuthor().getUsername())
        .isPublic(image.getVisibility() == Visibility.PUBLIC)
        .isLiked(isLiked)
        .countLikes(image.getLikeCount())
        .build();
  }

  public ImageResponseSimplified toImageResponseSimplified(BaseImage image, boolean isLiked) {
    return ImageResponseSimplified.builder()
        .id(image.getId())
        .isLiked(isLiked)
        .countLikes(image.getLikeCount())
        .build();
  }
}