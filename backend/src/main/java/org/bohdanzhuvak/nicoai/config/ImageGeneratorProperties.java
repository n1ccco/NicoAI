package org.bohdanzhuvak.nicoai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.image-generator")
@Getter
@Setter
public class ImageGeneratorProperties {
  private String url;
}
