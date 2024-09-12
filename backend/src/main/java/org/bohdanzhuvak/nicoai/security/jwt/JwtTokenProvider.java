package org.bohdanzhuvak.nicoai.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "roles";

  private final JwtProperties jwtProperties;

  private final UserDetailsService userDetailsService;

  public String generateAccessToken(UserDto userDto) {

    return generateToken(userDto, Date.from(LocalDateTime.now().plus(jwtProperties.getValidityAccess()).atZone(ZoneId.systemDefault()).toInstant()));
  }

  public String generateRefreshToken(UserDto userDto) {
    return generateToken(userDto, Date.from(LocalDateTime.now().plus(jwtProperties.getValidityRefresh()).atZone(ZoneId.systemDefault()).toInstant()));
  }

  private String generateToken(UserDto userDto, Date expiration) {

    String username = userDto.getUsername();
    List<String> authorities = userDto.getRoles();
    var claimsBuilder = Jwts.claims().subject(username);
    if (!authorities.isEmpty()) {
      claimsBuilder.add(AUTHORITIES_KEY, authorities);
    }
    var claims = claimsBuilder.build();

    Date now = new Date();

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(getSingInKey())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSingInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return claims.getSubject();
  }

  private SecretKey getSingInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser().verifyWith(getSingInKey()).build().parseSignedClaims(token).getPayload();

    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

    Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
        : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(getSingInKey()).build().parseSignedClaims(token);
      // parseClaimsJws will check expiration date. No need do here.
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}