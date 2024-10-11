package org.bohdanzhuvak.nicoai.dto.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseSimplified {
  private Long id;
  @JsonProperty(value = "isLiked")
  private boolean isLiked;
  private Integer countLikes;
  private byte[] imageData;
}
