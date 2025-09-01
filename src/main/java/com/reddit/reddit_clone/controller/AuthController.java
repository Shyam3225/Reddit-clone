package com.reddit.reddit_clone.controller;

import com.reddit.reddit_clone.dto.AuthenticationResponse;
import com.reddit.reddit_clone.dto.LoginRequest;
import com.reddit.reddit_clone.dto.RefreshTokenRequest;
import com.reddit.reddit_clone.dto.RegisterRequest;
import com.reddit.reddit_clone.service.AuthService;
import com.reddit.reddit_clone.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController

{
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
     public ResponseEntity<String> signup(@RequestBody @Valid RegisterRequest registerRequest)
    {
        authService.signup(registerRequest);
        return new ResponseEntity<>("user registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String>  verifyAccount(@PathVariable  @Valid String token)
    {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid LoginRequest loginRequest)
    {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted successfully!!");
    }

}
