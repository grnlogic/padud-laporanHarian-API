// File: RincianLaporanRequest.java
package com.padudjaya.laporan_harian_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RincianLaporanRequest {
    private String kategoriUtama;
    private String kategoriSub;
    private String keterangan;
    private BigDecimal nilaiRupiah;
    private BigDecimal nilaiKuantitas;
    private String satuan;
}