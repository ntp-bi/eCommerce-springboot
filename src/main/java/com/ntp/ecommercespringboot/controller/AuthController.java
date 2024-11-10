package com.ntp.ecommercespringboot.controller;

import com.ntp.ecommercespringboot.dto.ChangePasswordRequest;
import com.ntp.ecommercespringboot.dto.EmailConfirmationRequest;
import com.ntp.ecommercespringboot.dto.LoginRequest;
import com.ntp.ecommercespringboot.exception.ResourceNotFoundException;
import com.ntp.ecommercespringboot.model.User;
import com.ntp.ecommercespringboot.service.JwtService;
import com.ntp.ecommercespringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        final UserDetails userDetails = userService.getUserByEmail(loginRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.changePassword(email, request);
        return ResponseEntity.ok().body("Password changed");
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmationRequest request) {
        try {
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ResponseEntity.ok().body("Email confirmed successfully");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid confirmation code");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<String> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        if (user != null) {
            String role = String.valueOf(user.getRole());
            return ResponseEntity.ok(role);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserEmailById(@PathVariable Long id) {
        User user = userService.getUserEmailById(id);
        if (user != null) {
            return ResponseEntity.ok(user.getEmail());
        }

        return ResponseEntity.notFound().build();
    }
}