package org.bohdanzhuvak.nicoai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsProperties {
  private List<String> origins;
}