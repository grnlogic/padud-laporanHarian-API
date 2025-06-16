package com.padudjaya.laporan_harian_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.padudjaya.laporan_harian_api.model.RincianLaporan;

@Repository
public interface RincianLaporanRepository extends JpaRepository<RincianLaporan, Long> {
}