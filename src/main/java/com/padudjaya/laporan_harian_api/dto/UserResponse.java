// File: UserResponse.java
package com.padudjaya.laporan_harian_api.dto;

import com.padudjaya.laporan_harian_api.model.DivisiRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String fullName;
    private DivisiRole role;
}