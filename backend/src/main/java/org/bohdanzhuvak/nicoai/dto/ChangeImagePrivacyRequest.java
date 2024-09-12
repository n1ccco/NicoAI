package org.bohdanzhuvak.nicoai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeImagePrivacyRequest {
  @JsonProperty(value = "isPublic")
  private boolean isPublic;
}
