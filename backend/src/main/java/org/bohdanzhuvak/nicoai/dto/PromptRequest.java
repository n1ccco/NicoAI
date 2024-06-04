package org.bohdanzhuvak.nicoai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromptRequest {
  private String prompt;
  private String negativePrompt;
  private Integer height;
  private Integer width;
  private Integer numInterferenceSteps;
  private Integer guidanceScale;
}
