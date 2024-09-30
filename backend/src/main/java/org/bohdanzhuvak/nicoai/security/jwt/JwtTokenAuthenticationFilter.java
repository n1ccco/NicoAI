package org.bohdanzhuvak.nicoai.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.exception.InvalidTokenException;
import org.bohdanzhuvak.nicoai.exception.TokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.bohdanzhuvak.nicoai.security.jwt.TokenUtils.resolveToken;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilterInternal(@NonNull HttpServletRequest req,
                               @NonNull HttpServletResponse res,
                               @NonNull FilterChain filterChain)
      throws IOException, ServletException {

    String token = resolveToken(req);

    if (token != null) {
      try {
        jwtTokenProvider.validateToken(token);
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (TokenExpiredException | InvalidTokenException e) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");

        String responseBody = e.getMessage();
        res.getWriter().write(responseBody);
        res.getWriter().flush();
        return;
      } catch (Exception e) {
        throw new InvalidTokenException("Failed to process the access token");
      }
    }

    filterChain.doFilter(req, res);
  }

}