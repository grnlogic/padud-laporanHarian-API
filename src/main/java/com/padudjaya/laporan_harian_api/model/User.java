// Lokasi File: src/main/java/com/padudjaya/laporan_harian_api/model/User.java
package com.padudjaya.laporan_harian_api.model;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails { // Implementasi UserDetails untuk keamanan

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // Ini akan berisi password yang sudah di-hash

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // KITA HANYA GUNAKAN SATU FIELD INI UNTUK PERAN/DIVISI
    // Ini sesuai dengan kolom 'role' di database Anda.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DivisiRole role;

    @Column(name = "created_at", updatable = false)
    @Builder.Default // Agar nilai default ini terisi saat menggunakan @Builder
    private Instant createdAt = Instant.now();

    // --- Metode-metode yang wajib ada dari implementasi UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mengubah 'role' kita menjadi format yang dimengerti Spring Security
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Untuk simplifikasi, kita anggap akun selalu aktif dan tidak terkunci.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}