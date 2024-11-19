package org.bohdanzhuvak.nicoai.features.images.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagesResponse {
  private List<ImageResponseSimplified> data;
  private Meta meta;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Meta {
    private int page;
    private long total;
    private int totalPages;
  }
}