package org.bohdanzhuvak.nicoai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageBlobResponse {
  private byte[] imageBlob;
}
