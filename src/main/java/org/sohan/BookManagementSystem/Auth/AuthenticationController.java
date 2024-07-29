package org.sohan.BookManagementSystem.Auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sohan.BookManagementSystem.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public  ResponseEntity<User> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        User register = service.register(request);
        return new ResponseEntity<>(register, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        AuthenticationResponse authenticate = service.authenticate(request);
        return new ResponseEntity<>(authenticate,HttpStatus.OK);
    }
    @GetMapping("/activateAccount")
    public ResponseEntity<String> confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
        return new ResponseEntity<>("Account Activated",HttpStatus.OK);
    }

}
