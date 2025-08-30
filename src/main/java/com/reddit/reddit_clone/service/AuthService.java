package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.dto.AuthenticationResponse;
import com.reddit.reddit_clone.dto.LoginRequest;
import com.reddit.reddit_clone.dto.RegisterRequest;
import com.reddit.reddit_clone.exception.SpringRedditException;
import com.reddit.reddit_clone.model.NotificationEmail;
import com.reddit.reddit_clone.model.User;
import com.reddit.reddit_clone.model.VerificationToken;
import com.reddit.reddit_clone.repository.UserRepository;
import com.reddit.reddit_clone.repository.VerificationTokenRepository;
import com.reddit.reddit_clone.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    // Better way is to use construction injection
    // make them final use the constructor
    // simply use @AllArgsConstructor and make them final

    //  private final UserRepository userRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest)
    {
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(
                "Please Activate your account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit," + "please click on the below url to activate your account /n" +
                        "http://localhost:8080/api/auth/accountVerification/"+ token));

    }

    private String generateVerificationToken(User user) {
        String token= UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
       Optional<VerificationToken> verificationTokenOptional =  verificationTokenRepository.findByToken(token);
       verificationTokenOptional.orElseThrow(()-> new SpringRedditException("Invalid token"));

       fetchUserAndEnable(verificationTokenOptional.get());

    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUserName();

        User userRepositoryByUserName = userRepository.findByUserName(userName).orElseThrow(()-> new SpringRedditException("user not found with name"+ userName));

        userRepositoryByUserName.setEnabled(true);
        userRepository.save(userRepositoryByUserName);


    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String generatedToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(generatedToken,loginRequest.getUserName());
    }

    @Transactional
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

}
