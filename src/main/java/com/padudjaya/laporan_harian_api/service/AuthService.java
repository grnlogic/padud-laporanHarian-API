package com.padudjaya.laporan_harian_api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final JwtService jwtService; // Tambahkan ini
    private final AuthenticationManager authenticationManager; // Tambahkan ini

    @Transactional
    public void register(RegisterRequest registerRequest) {
        // 1. Cek apakah username sudah ada
        userRepository.findByUsername(registerRequest.getUsername())
            .ifPresent(user -> {
                throw new IllegalStateException("Username sudah terdaftar!");
            });

        // 2. Buat user baru dan PASTIKAN password di-enkripsi
        User newUser = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .role(registerRequest.getRole())
                .build();

        // 3. Simpan user baru ke database
        userRepository.save(newUser);
    }

    // --- METODE BARU UNTUK LOGIN ---
    public AuthResponse login(LoginRequest loginRequest) {
        // 1. Otentikasi pengguna
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        // 2. Jika berhasil, cari user di database
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalStateException("User tidak ditemukan setelah otentikasi"));
        
        // 3. Buat JWT Token
        var jwtToken = jwtService.generateToken(user);
        
        // 4. Kirim token sebagai respons
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}