package com.micro.Service;

import com.micro.Entity.User;
import com.micro.config.JwtUtils;
import com.micro.model.LoginRequest;
import com.micro.model.LoginResponse;
import com.micro.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtUtils;

  @Autowired
  private final AuthenticationManager authenticationManager;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
    this.authenticationManager = authenticationManager;
  }

  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() ->
        new RuntimeException("User not found"));
  }


  public LoginResponse login(LoginRequest authRequest) {
    User appUser = userRepository.findByUsername(authRequest.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("Bad credentials, please try again."));

    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
              authRequest.getPassword()));
    } catch (org.springframework.security.authentication.BadCredentialsException ex) {
      throw new BadCredentialsException("Bad credentials, please try again.");
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);

    if (authentication.isAuthenticated()) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());

      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setUsername(userDetails.getUsername());
      loginResponse.setToken(jwtUtils.generateToken(authRequest.getUsername(), appUser.getId()));
      loginResponse.setRole(roles);
      return loginResponse;
    } else {
      throw new UsernameNotFoundException("Invalid user request!");
    }
  }


}
