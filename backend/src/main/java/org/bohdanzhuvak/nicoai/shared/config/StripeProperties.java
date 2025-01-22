package org.bohdanzhuvak.nicoai.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
@Data
public class StripeProperties {
  private String secret_key;
  private String public_key;
  private String webhook_key;
}
