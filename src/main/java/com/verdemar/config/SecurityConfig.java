package com.verdemar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()); // opcional, según tu caso
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers("/api/beds/**", "/api/beds").denyAll().anyRequest().permitAll());

    return http.build();
  }
}
