// File: UpdateUserRequest.java
package com.padudjaya.laporan_harian_api.dto;

import com.padudjaya.laporan_harian_api.model.DivisiRole;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private DivisiRole role;
    private String password; // Tambahkan field ini untuk update password
}