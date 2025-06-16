// File: AuthResponse.java (DIPERBARUI)
package com.padudjaya.laporan_harian_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String fullName;
    // Kita ubah tipe role menjadi String agar mudah dibaca di Front-End
    private String role; 
    private String userId;
}