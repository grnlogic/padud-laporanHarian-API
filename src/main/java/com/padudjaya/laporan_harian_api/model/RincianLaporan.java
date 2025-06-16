// File: RincianLaporan.java
package com.padudjaya.laporan_harian_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rincian_laporan")
public class RincianLaporan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rincian_id")
    private Long rincianId;

    // Relasi: Banyak Rincian dimiliki oleh SATU Laporan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laporan_id", nullable = false)
    private LaporanHarian laporan;

    @Column(name = "kategori_utama")
    private String kategoriUtama;

    @Column(name = "kategori_sub")
    private String kategoriSub;

    private String keterangan;

    @Column(name = "nilai_rupiah")
    private BigDecimal nilaiRupiah;

    @Column(name = "nilai_kuantitas")
    private BigDecimal nilaiKuantitas;

    private String satuan;
}