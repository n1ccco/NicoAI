package org.bohdanzhuvak.nicoai.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secretKey = "azxxszyykpbgqcfgfgsqcyvyhajt";

    // validity in milliseconds
    private long validityInMs = 3600000; // 1h

}