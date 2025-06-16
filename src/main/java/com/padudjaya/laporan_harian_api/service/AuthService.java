// File: AuthService.java (KODE LENGKAP & SUDAH DIPERBAIKI)
package com.padudjaya.laporan_harian_api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.padudjaya.laporan_harian_api.dto.AuthResponse;
import com.padudjaya.laporan_harian_api.dto.LoginRequest;
import com.padudjaya.laporan_harian_api.dto.RegisterRequest;
import com.padudjaya.laporan_harian_api.model.User;
import com.padudjaya.laporan_harian_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        // Cek apakah user sudah ada
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username sudah digunakan");
        }

        // Buat user baru. Logikanya jadi lebih simpel!
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .role(request.getRole()) // Langsung ambil role dari request
            .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .fullName(user.getFullName())
            .role(user.getRole().name()) // Konversi enum ke string untuk respons
            .userId(user.getUserId().toString())
            .build();
    }
}