package org.bohdanzhuvak.nicoai.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class TokenUtils {
  public static final String HEADER_PREFIX = "Bearer ";

  public static String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
