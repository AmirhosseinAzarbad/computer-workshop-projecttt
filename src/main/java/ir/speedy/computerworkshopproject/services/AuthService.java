package ir.speedy.computerworkshopproject.services;

import ir.speedy.computerworkshopproject.Repositories.AdminRegPassRepository;
import ir.speedy.computerworkshopproject.Repositories.UserRepository;
import ir.speedy.computerworkshopproject.dto.auth.RegisterRequest;
import ir.speedy.computerworkshopproject.enums.Role;
import ir.speedy.computerworkshopproject.models.AdminRegPass;
import ir.speedy.computerworkshopproject.models.User;
import ir.speedy.computerworkshopproject.security.jwt.JwtTokenProvider;
import ir.speedy.computerworkshopproject.utils.PhoneNumberUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AuthService {
    @Value("${app.owner.registration.password}")
    private  String ownerRegistrationPass;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRegPassRepository adminRegPassRepository;
    private final OtpService otpService;

    public AuthService(
                       JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AdminRegPassRepository adminRegPassRepository,
                       OtpService otpService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRegPassRepository = adminRegPassRepository;
        this.otpService = otpService;
    }

    public void sendOtp(String phoneNumber) {
        otpService.generateAndSendOtp(phoneNumber);
    }

    public User register(RegisterRequest request , String otp){

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        String normalizedPhone = PhoneNumberUtil.normalizePhoneNumber(user.getPhoneNumber());
        user.setPhoneNumber(normalizedPhone);

        if(!otpService.validateOtp(normalizedPhone,otp)){
            throw new IllegalArgumentException("Invalid Or Expired OTP");
        }

        if (user.getRole()== Role.OWNER)
            return registerOwner(user);
        else if (user.getRole()== Role.ADMIN)
            return registerAdmin(user);
        if(user.getPassword() == null){
            throw new IllegalArgumentException("Password Can't be Null");
        }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        otpService.invalidateOtp(normalizedPhone);
        return userRepository.save(user);
    }

    private User registerOwner(User user) {
        if (userRepository.existsByRole(Role.OWNER)) {
            throw new IllegalStateException("Only one OWNER is allowed.");
        }
        if (!user.getPassword().equals(ownerRegistrationPass)) {
            throw new IllegalArgumentException("Incorrect owner registration password.");
        }
        if (adminRegPassRepository.count() == 0) {
            AdminRegPass adminRegPass = new AdminRegPass(passwordEncoder.encode("adminforsneakershop"));
            adminRegPassRepository.save(adminRegPass);
        }
        user.setRole(Role.OWNER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private User registerAdmin(User user) {
        if (!userRepository.existsByRole(Role.OWNER)) {
            throw new IllegalStateException("No OWNER registered yet. ADMIN registration is not allowed.");
        }
        AdminRegPass adminRegPass = adminRegPassRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new EntityNotFoundException("System adminRegPass not found!"));
        if (!passwordEncoder.matches(user.getPassword(), adminRegPass.getAdminRegistrationPassword())) {
            throw new IllegalArgumentException("Incorrect admin registration password.");
        }
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return jwtTokenProvider.generateToken(username);
    }

}
