// File: UpdateLaporanRequest.java
package com.padudjaya.laporan_harian_api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class UpdateLaporanRequest {
    private LocalDate tanggalLaporan;
    private List<RincianLaporanRequest> rincian;
}