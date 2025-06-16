// File: LaporanHarianResponse.java
package com.padudjaya.laporan_harian_api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaporanHarianResponse {
    private Long laporanId;
    private String submittedBy; // Nama lengkap pengirim
    private String divisi;
    private LocalDate tanggalLaporan;
    private List<RincianLaporanResponse> rincian;
}