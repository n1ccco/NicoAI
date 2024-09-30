package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.config.ImageGeneratorProperties;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.exception.ImageGenerationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ImageGeneratorService {
  private final ImageGeneratorProperties imageGeneratorProperties;
  private final RestTemplate restTemplate;

  public byte[] fetchImageFromGenerator(PromptRequest promptRequest) {
    URI uri = buildUri(promptRequest);

    try {
      RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
      ResponseEntity<byte[]> response = restTemplate.exchange(requestEntity, byte[].class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        return response.getBody();
      } else {
        throw new ImageGenerationException("Received non-successful HTTP status " + response.getStatusCode());
      }
    } catch (RestClientException e) {
      throw new ImageGenerationException("Failed to generate image: " + e.getMessage());
    }
  }

  private URI buildUri(PromptRequest promptRequest) {
    MultiValueMap<String, String> params = promptRequest.toMultiValueMap();

    return UriComponentsBuilder.fromHttpUrl(imageGeneratorProperties.getUrl())
        .pathSegment("generate")
        .queryParams(params)
        .build()
        .toUri();
  }

}
