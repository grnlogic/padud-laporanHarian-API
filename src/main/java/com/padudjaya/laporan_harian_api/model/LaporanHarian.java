// File: LaporanHarian.java
package com.padudjaya.laporan_harian_api.model;
import jakarta.persistence.FetchType;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "laporan_harian")
public class LaporanHarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laporan_id")
    private Long laporanId;

    // Relasi: Banyak Laporan dimiliki oleh SATU User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tanggal_laporan", nullable = false)
    private LocalDate tanggalLaporan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DivisiRole divisi;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    // Relasi: SATU Laporan memiliki BANYAK Rincian
    @OneToMany(mappedBy = "laporan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // <-- TAMBAHKAN INI
private List<RincianLaporan> rincianList;
}