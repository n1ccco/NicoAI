package org.bohdanzhuvak.nicoai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "image")
@Data
public class ImageProperties {
  private String FOLDER_PATH;
}
