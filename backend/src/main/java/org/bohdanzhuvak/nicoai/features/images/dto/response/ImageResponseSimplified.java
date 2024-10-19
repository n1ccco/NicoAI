package org.bohdanzhuvak.nicoai.features.images.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ImageResponseSimplified implements Serializable {
  private Long id;
  @JsonProperty(value = "isLiked")
  private boolean isLiked;
  private Long countLikes;
}
