package org.bohdanzhuvak.nicoai.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("prompt", prompt);
    params.add("negativePrompt", negativePrompt);
    params.add("height", String.valueOf(height));
    params.add("width", String.valueOf(width));
    params.add("numInterferenceSteps", String.valueOf(numInterferenceSteps));
    params.add("guidanceScale", String.valueOf(guidanceScale));
    return params;
  }
}
