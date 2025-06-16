package com.padudjaya.laporan_harian_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.padudjaya.laporan_harian_api.model.LaporanHarian;
import com.padudjaya.laporan_harian_api.model.User;

@Repository
public interface LaporanHarianRepository extends JpaRepository<LaporanHarian, Long> {
    // Metode baru untuk mencari semua laporan milik satu user, diurutkan dari terbaru
    List<LaporanHarian> findByUserOrderByTanggalLaporanDesc(User user);
}