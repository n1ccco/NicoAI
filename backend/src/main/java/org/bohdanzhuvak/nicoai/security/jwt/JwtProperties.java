package org.bohdanzhuvak.nicoai.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

  private String secret;

  // validity in milliseconds
  private Date validityAccessInMs = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()); // 1h
  private Date validityRefreshInMs = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

}