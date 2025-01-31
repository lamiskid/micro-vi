package com.capstoneproject.config;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class GateWayConfig {

  @Bean
  public CorsWebFilter corsWebFilter() {


      final CorsConfiguration corsConfig = new CorsConfiguration();
      corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:3000"));
      corsConfig.setAllowCredentials(true);
      corsConfig.setMaxAge(3600L);
      corsConfig.setAllowedMethods(Arrays.asList("GET", "POST","DELETE"));
      corsConfig.addAllowedHeader("*");

      final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", corsConfig);

      return new CorsWebFilter(source);

  }
}




