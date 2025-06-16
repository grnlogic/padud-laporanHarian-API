// File: LoginRequest.java
package com.padudjaya.laporan_harian_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}