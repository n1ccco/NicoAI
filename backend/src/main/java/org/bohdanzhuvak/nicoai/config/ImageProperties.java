package org.bohdanzhuvak.nicoai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "image")
@Getter
@Setter
public class ImageProperties {
  private String FOLDER_PATH;
}
