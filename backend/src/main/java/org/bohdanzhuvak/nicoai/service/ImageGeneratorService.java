package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.config.ImageGeneratorProperties;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ImageGeneratorService {
  private final ImageGeneratorProperties imageGeneratorProperties;
  private final RestTemplate restTemplate;


  private MultiValueMap<String, String> buildPromptParams(PromptRequest promptRequest) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("prompt", promptRequest.getPrompt());
    params.add("negativePrompt", promptRequest.getNegativePrompt());
    params.add("height", String.valueOf(promptRequest.getHeight()));
    params.add("width", String.valueOf(promptRequest.getWidth()));
    params.add("numInterferenceSteps", String.valueOf(promptRequest.getNumInterferenceSteps()));
    params.add("guidanceScale", String.valueOf(promptRequest.getGuidanceScale()));
    return params;
  }

  public byte[] fetchImageFromGenerator(PromptRequest promptRequest) {
    String uri = UriComponentsBuilder.fromHttpUrl(imageGeneratorProperties.getUrl())
        .pathSegment("generate")
        .queryParams(buildPromptParams(promptRequest))
        .build()
        .toUriString();
    return restTemplate.getForObject(uri, byte[].class);
  }

}
