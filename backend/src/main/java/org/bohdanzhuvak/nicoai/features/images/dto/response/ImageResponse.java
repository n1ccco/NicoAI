package org.bohdanzhuvak.nicoai.features.images.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
  private Long id;
  @JsonProperty(value = "isPublic")
  private boolean isPublic;
  @JsonProperty(value = "isLiked")
  private boolean isLiked;
  private Long countLikes;
  private Long authorId;
  private String authorName;
}
