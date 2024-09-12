package org.bohdanzhuvak.nicoai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "image")
@Getter
@Setter
public class ImageProperties {
  private String FOLDER_PATH;
}
