package org.bohdanzhuvak.nicoai.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.bohdanzhuvak.nicoai.utils.TokenUtils.resolveToken;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilterInternal(@NonNull HttpServletRequest req,
                               @NonNull HttpServletResponse res,
                               @NonNull FilterChain filterChain)
      throws IOException, ServletException {

    String token = resolveToken(req);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication auth = jwtTokenProvider.getAuthentication(token);

      if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
      }
    }

    filterChain.doFilter(req, res);
  }

}