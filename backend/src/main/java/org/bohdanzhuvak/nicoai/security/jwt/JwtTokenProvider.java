package org.bohdanzhuvak.nicoai.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "roles";

  private final JwtProperties jwtProperties;

  private final UserDetailsService userDetailsService;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64.getEncoder().encodeToString(this.jwtProperties.getSecretKey().getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(Authentication authentication) {

    String username = authentication.getName();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    var claimsBuilder = Jwts.claims().subject(username);
    if (!authorities.isEmpty()) {
      claimsBuilder.add(AUTHORITIES_KEY,
          authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
    }

    var claims = claimsBuilder.build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(validity)
        .signWith(this.secretKey, Jwts.SIG.HS256)
        .compact();

  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();

    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

    Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
        : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
      // parseClaimsJws will check expiration date. No need do here.
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

}