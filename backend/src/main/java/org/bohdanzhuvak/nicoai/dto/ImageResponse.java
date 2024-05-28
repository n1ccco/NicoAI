package org.bohdanzhuvak.nicoai.dto;

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
  private String description;
  @JsonProperty(value = "isPublic")
  private boolean isPublic;
  @JsonProperty(value = "isLiked")
  private boolean isLiked;
  private Long authorId;
  private byte[] imageData;
}
