package com.example.todomongodb.controller;

import com.example.todomongodb.entity.Account;
import com.example.todomongodb.entity.User;
import com.example.todomongodb.enumEntity.RoleType;
import com.example.todomongodb.jwt.JwtUtils;
import com.example.todomongodb.repository.UserRepository;
import com.example.todomongodb.request.TokenRefreshRequest;
import com.example.todomongodb.repository.AccountRepository;
import com.example.todomongodb.request.ChangePasswordRequest;
import com.example.todomongodb.request.LoginRequest;
import com.example.todomongodb.request.SignupRequest;
import com.example.todomongodb.response.JwtResponse;
import com.example.todomongodb.response.MessageResponse;
import com.example.todomongodb.response.TokenRefreshResponse;
import com.example.todomongodb.service.AccountService;
import com.example.todomongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/{id}")
    public Account getAccountByUsername(@PathVariable("id") String username){
        return accountRepository.findByUsername(username).get();
    }
    @GetMapping("/check/{phone}")
    public ResponseEntity<?> checkPhoneNumber(@PathVariable("phone") String phone){
        if (accountRepository.existsByUsername(phone)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
        // nếu tài khoản tồn tại
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã tồn tại!"));
        }

        // đăng ký
        Account account = new Account();
        account.setUsername(signUpRequest.getUsername());
        account.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        account.setRoles(RoleType.ROLE_USER.toString());
        accountRepository.save(account);

        // tạo user sau khi đăng ký
        User newUser = User.builder()
                .name(signUpRequest.getFullname())
                .phone(account.getUsername())
                .email(signUpRequest.getEmail())
                .address("Chưa có")
                .image("https://chatappvalo.s3.ap-southeast-1.amazonaws.com/avatar.jpg")
                .build();
        userService.save(newUser);

        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Đăng nhập thất bại"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = (Account) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        //generate a refresh token for refresh access token
        String refreshToken = jwtUtils.generateJwtRefreshToken(account.getUsername());
        accountService.setRefreshToken(account.getUsername(), refreshToken);
        User user = userRepository.findDistinctByPhone(account.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản " + account.getUsername()));
        return ResponseEntity.ok(new JwtResponse(jwt,refreshToken, user, account));
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();
        if (requestRefreshToken != null && jwtUtils.validateJwtToken(requestRefreshToken)){
            String username = jwtUtils.getUserNameFromJwtToken(requestRefreshToken);
            System.out.println(username);
            Account account = accountRepository.findByUsername(username).get();
            System.out.println(account);
            if (account != null && requestRefreshToken.equals(account.getRefreshToken())){
                String newAccessToken = jwtUtils.generateJwtTokenWithAccountId(account.getUsername());
                String newRefreshToken = jwtUtils.generateJwtRefreshToken(account.getUsername());
                accountService.setRefreshToken(account.getUsername(), newRefreshToken);
                return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("User Not Found"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid request refresh token"));
    }

    @PostMapping("/signout")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> signout(@Valid @RequestBody TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();
        if (requestRefreshToken != null && jwtUtils.validateJwtToken(requestRefreshToken)){
            String username = jwtUtils.getUserNameFromJwtToken(requestRefreshToken);
            System.out.println(username);
            Account account = accountRepository.findByUsername(username).get();
            System.out.println(account);
            if (account != null && requestRefreshToken.equals(account.getRefreshToken())){
                String token = jwtUtils.generateJwtTokenWithAccountId(account.getUsername());

                // set null to refresh token
                accountService.setRefreshToken(account.getUsername(), null);
                return ResponseEntity.ok(new MessageResponse("Signout Successfully!!"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("User Not Found"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid signout request"));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        Account account = accountRepository.findByUsername(changePasswordRequest.getPhoneNumber()).get();
        account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        accountRepository.save(account);
        return ResponseEntity.ok(new MessageResponse("Đổi mật khẩu thành công"));
    }
}

