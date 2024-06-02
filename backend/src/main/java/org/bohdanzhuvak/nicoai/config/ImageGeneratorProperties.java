package org.bohdanzhuvak.nicoai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "api.image-generator")
@Getter
@Setter
public class ImageGeneratorProperties {
  private String url;
}
