package ir.speedy.computerworkshopproject.controller;


import ir.speedy.computerworkshopproject.dto.auth.LoginRequest;
import ir.speedy.computerworkshopproject.dto.auth.RegisterRequest;
import ir.speedy.computerworkshopproject.dto.phoneNumber.PhoneNumberRequest;
import ir.speedy.computerworkshopproject.models.User;
import ir.speedy.computerworkshopproject.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody PhoneNumberRequest request) {
        authService.sendOtp(request.getPhoneNumber());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP sent to phone number "+ request.getPhoneNumber() + " Please verify before registration.");
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid@RequestBody RegisterRequest request) {

        User user = authService.register(request, request.getOtp());
        return ResponseEntity.status(HttpStatus.OK).body(user.getRole()+ " registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Login successful. Token: " + token);
    }
}
