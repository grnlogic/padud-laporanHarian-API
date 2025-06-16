// File: src/main/java/com/padudjaya/laporan_harian_api/controller/AuthController.java
package com.padudjaya.laporan_harian_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padudjaya.laporan_harian_api.dto.AuthResponse;
import com.padudjaya.laporan_harian_api.dto.LoginRequest;
import com.padudjaya.laporan_harian_api.dto.RegisterRequest;
import com.padudjaya.laporan_harian_api.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // --- METODE REGISTER YANG DIPERBAIKI ---
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("User berhasil didaftarkan!");
    }

    // --- ENDPOINT BARU UNTUK LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // Untuk login, kita langsung kembalikan hasilnya karena service akan
        // melempar error jika login gagal, yang akan ditangani otomatis oleh Spring Security.
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}