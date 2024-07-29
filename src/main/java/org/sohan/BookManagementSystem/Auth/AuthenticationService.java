package org.sohan.BookManagementSystem.Auth;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.sohan.BookManagementSystem.email.EmailService;
import org.sohan.BookManagementSystem.email.EmailTemplateName;
import org.sohan.BookManagementSystem.role.Role;
import org.sohan.BookManagementSystem.role.RoleRepository;
import org.sohan.BookManagementSystem.security.JwtService;
import org.sohan.BookManagementSystem.user.Token;
import org.sohan.BookManagementSystem.user.TokenRepository;
import org.sohan.BookManagementSystem.user.User;
import org.sohan.BookManagementSystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activationUrl}")
    private String activationUrl;

    public User register(RegistrationRequest request) throws MessagingException {
        Role role = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("Role Not Initialized"));
        User user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(role))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
        return user;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newTokenForRegistration  = generateAndSaveActivationToken(user);
        //send email to user
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newTokenForRegistration,
                "Account Activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        //generate a token
        String generateToken =  generateActivationCode(6);
        Token token = Token.builder().token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int parameter) {
        StringBuilder stringBuilder = new StringBuilder();
        String characters = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0 ; i < parameter; i++) {
            stringBuilder.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = (User) authenticate.getPrincipal();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("fullName",user.getFullName());

        String tokenWithExtraClaims = jwtService.generateToken(stringObjectHashMap, user);

//        if(user.isAccountLocked()) {
//            throw new IllegalStateException("Account Locked");
//        }

        return new AuthenticationResponse(tokenWithExtraClaims);
    }

    public void activateAccount(String token) throws MessagingException {
        Token t = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid Token"));
        if(LocalDateTime.now().isAfter(t.getExpiresAt())){
            sendValidationEmail(t.getUser());
            throw new RuntimeException("Activation Token has expired. Try again");
        }
        User user = userRepository.findById(t.getUser().getId())
                .orElseThrow(() -> new RuntimeException("ID NOT FOUND"));
        user.setEnabled(true);
        userRepository.save(user);
        t.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(t);
    }
}
