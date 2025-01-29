package com.capstoneproject.filter;

import com.capstoneproject.exception.MissingAuthorizationHeaderException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


import org.springframework.http.HttpHeaders;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {


  @Autowired
  private RouteValidator validator;

  @Autowired
  private JwtUtil jwtUtil;

  public AuthenticationFilter() {
    super(NameConfig.class);

  }

  @Override
  public GatewayFilter apply(NameConfig config) {
    return ((exchange, chain) ->{
      ServerHttpRequest serverHttpRequest = null;

      if (validator.isSecured.test(exchange.getRequest())) {
        //header contains token or not
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
          throw new MissingAuthorizationHeaderException("Missing authorization header.");
        }

        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
          authHeader = authHeader.substring(7);
        }
        try {
          jwtUtil.validateToken(authHeader);
           serverHttpRequest= exchange.getRequest().mutate()
              .header("loggedInUser", jwtUtil.extractUsername(authHeader)).build();

        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("Invalid access...!");
          throw new RuntimeException("Unauthorised access to application.");
        }
      }
      return chain.filter(exchange.mutate().request(serverHttpRequest).build());

    });
  }
}


