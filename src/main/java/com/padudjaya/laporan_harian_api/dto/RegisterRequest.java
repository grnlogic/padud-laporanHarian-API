// File: src/main/java/com/padudjaya/laporan_harian_api/dto/RegisterRequest.java
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
    private DivisiRole role;
}