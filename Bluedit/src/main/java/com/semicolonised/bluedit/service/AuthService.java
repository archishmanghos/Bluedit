package com.semicolonised.bluedit.service;

import com.semicolonised.bluedit.dto.AuthenticationResponse;
import com.semicolonised.bluedit.dto.LoginRequest;
import com.semicolonised.bluedit.dto.RegisterRequest;
import com.semicolonised.bluedit.exception.BlueditException;
import com.semicolonised.bluedit.model.NotificationEmail;
import com.semicolonised.bluedit.model.User;
import com.semicolonised.bluedit.model.VerificationToken;
import com.semicolonised.bluedit.repository.UserRepository;
import com.semicolonised.bluedit.repository.VerificationTokenRepository;
import com.semicolonised.bluedit.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        log.info("User Registered Successfully, Sending Authentication Email");
        String verificationToken = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(
                "Bluedit: Please Activate Account",
                user.getEmail(),
                "Thank you for signing up to Bluedit, "
                        + "Please click on this url to activate your account: "
                        + "http://localhost:8080/api/auth/accountVerification/"
                        + verificationToken
        ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional =
                verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationTokenOptional.orElseThrow(() -> new BlueditException("Invalid token")));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BlueditException("User not found with username : " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtProvider.generateToken(userDetails);

        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
