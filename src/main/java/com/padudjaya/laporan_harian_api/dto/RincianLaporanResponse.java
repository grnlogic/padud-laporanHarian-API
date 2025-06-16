// File: RincianLaporanResponse.java
package com.padudjaya.laporan_harian_api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RincianLaporanResponse {
    private Long rincianId;
    private String kategoriUtama;
    private String kategoriSub;
    private String keterangan;
    private BigDecimal nilaiRupiah;
    private BigDecimal nilaiKuantitas;
    private String satuan;
}