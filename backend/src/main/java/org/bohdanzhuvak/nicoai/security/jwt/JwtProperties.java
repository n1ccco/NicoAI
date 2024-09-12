package org.bohdanzhuvak.nicoai.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

  private String secret;

  // validity in milliseconds
  private Duration validityAccess = Duration.ofMinutes(10);
  private Duration validityRefresh = Duration.ofDays(3);

}