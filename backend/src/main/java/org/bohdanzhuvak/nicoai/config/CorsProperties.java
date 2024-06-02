package org.bohdanzhuvak.nicoai.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsProperties {
  private List<String> origins;
}