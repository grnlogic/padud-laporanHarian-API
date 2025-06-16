// File: RegisterRequest.java (DIPERBARUI)
package com.padudjaya.laporan_harian_api.dto;

import com.padudjaya.laporan_harian_api.model.DivisiRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    // Langsung terima role sebagai enum, tidak perlu field division
    private DivisiRole role; 
}