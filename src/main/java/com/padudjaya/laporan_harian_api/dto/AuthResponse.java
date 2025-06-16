// File: AuthResponse.java
package com.padudjaya.laporan_harian_api.dto;

import com.padudjaya.laporan_harian_api.model.DivisiRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private DivisiRole role;
}