package org.bohdanzhuvak.nicoai.config;

import org.bohdanzhuvak.nicoai.model.CustomUserDetails;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenAuthenticationFilter;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain springWebFilterChain(HttpSecurity http,
      JwtTokenProvider tokenProvider) throws Exception {
    return http
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/auth/signin").permitAll()
            .requestMatchers("/api/auth/signup").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/images").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/api/images/*/comments").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  UserDetailsService customUserDetailsService(UserRepository users) {
    return (username) -> new CustomUserDetails(users.findByUsername(username));
  }

  @Bean
  AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder encoder) {
    return authentication -> {
      String username = authentication.getPrincipal() + "";
      String password = authentication.getCredentials() + "";

      UserDetails user = userDetailsService.loadUserByUsername(username);

      if (!encoder.matches(password, user.getPassword())) {
        throw new BadCredentialsException("Bad credentials");
      }

      if (!user.isEnabled()) {
        throw new DisabledException("User account is not active");
      }

      return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    };
  }

}