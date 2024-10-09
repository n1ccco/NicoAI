package org.bohdanzhuvak.nicoai.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;
import org.bohdanzhuvak.nicoai.exception.InvalidTokenException;
import org.bohdanzhuvak.nicoai.exception.TokenExpiredException;
import org.bohdanzhuvak.nicoai.security.CustomUserDetails;
import org.bohdanzhuvak.nicoai.security.CustomUserServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "roles";

  private final JwtProperties jwtProperties;

  private final CustomUserServiceImpl userDetailsService;

  public String generateAccessToken(UserDto userDto) {

    return generateToken(userDto, Date.from(LocalDateTime.now().plus(jwtProperties.getValidityAccess()).atZone(ZoneId.systemDefault()).toInstant()));
  }

  public String generateRefreshToken(UserDto userDto) {
    return generateToken(userDto, Date.from(LocalDateTime.now().plus(jwtProperties.getValidityRefresh()).atZone(ZoneId.systemDefault()).toInstant()));
  }

  private String generateToken(UserDto userDto, Date expiration) {

    Map<String, Object> claims = new HashMap<>();
    claims.put(Claims.SUBJECT, userDto.getUsername());

    if (!userDto.getRoles().isEmpty()) {
      String authorities = String.join(",", userDto.getRoles());
      claims.put(AUTHORITIES_KEY, authorities);
    }

    return Jwts.builder()
        .claims(claims)
        .issuedAt(new Date())
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

    CustomUserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSingInKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new TokenExpiredException("The access token has expired");
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidTokenException("The access token is invalid");
    }
  }
}