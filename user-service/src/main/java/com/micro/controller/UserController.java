package com.micro.controller;

import com.micro.Entity.User;
import com.micro.Service.UserService;
import com.micro.model.LoginRequest;
import com.micro.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public User register(@RequestBody User user) {
    return userService.register(user);
  }

  @GetMapping("/{username}")
  public User getUser(@PathVariable String username) {
    return userService.findByUsername(username);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticateAndGetToken(@RequestBody LoginRequest authRequest) {

    return ResponseEntity.ok(userService.login(authRequest));
  }
}