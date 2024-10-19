package org.bohdanzhuvak.nicoai.features.images.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageBlobResponse {
  private byte[] imageBlob;
}
