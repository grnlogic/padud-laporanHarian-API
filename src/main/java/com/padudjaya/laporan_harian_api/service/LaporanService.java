// File: LaporanService.java
package com.padudjaya.laporan_harian_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padudjaya.laporan_harian_api.dto.LaporanHarianRequest;
import com.padudjaya.laporan_harian_api.dto.LaporanHarianResponse;
import com.padudjaya.laporan_harian_api.dto.RincianLaporanResponse;
import com.padudjaya.laporan_harian_api.dto.UpdateLaporanRequest;
import com.padudjaya.laporan_harian_api.model.LaporanHarian;
import com.padudjaya.laporan_harian_api.model.RincianLaporan;
import com.padudjaya.laporan_harian_api.model.User;
import com.padudjaya.laporan_harian_api.repository.LaporanHarianRepository;
import com.padudjaya.laporan_harian_api.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LaporanService {

    private final LaporanHarianRepository laporanHarianRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createLaporan(LaporanHarianRequest request, String username) {
        // 1. Cari user yang sedang login berdasarkan username dari token
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan: " + username));

        // 2. Buat objek LaporanHarian (induknya)
        LaporanHarian laporanHarian = LaporanHarian.builder()
                .user(currentUser)
                .tanggalLaporan(request.getTanggalLaporan())
                .divisi(currentUser.getRole())
                .build();

        // 3. Buat daftar objek RincianLaporan (anak-anaknya) dari request
        List<RincianLaporan> rincianList = request.getRincian().stream()
                .map(rincianDto -> RincianLaporan.builder()
                        .kategoriUtama(rincianDto.getKategoriUtama())
                        .kategoriSub(rincianDto.getKategoriSub())
                        .keterangan(rincianDto.getKeterangan())
                        .nilaiRupiah(rincianDto.getNilaiRupiah())
                        .nilaiKuantitas(rincianDto.getNilaiKuantitas())
                        .satuan(rincianDto.getSatuan())
                        .laporan(laporanHarian) // Hubungkan setiap anak ke induknya
                        .build())
                .collect(Collectors.toList());

        // 4. Masukkan daftar rincian ke dalam laporan induk
        laporanHarian.setRincianList(rincianList);

        // 5. Simpan laporan induk. Karena ada `CascadeType.ALL`, semua rincian akan ikut tersimpan.
        laporanHarianRepository.save(laporanHarian);
    }

    // --- METODE-METODE BARU ---

    public List<LaporanHarianResponse> getAllLaporanForCurrentUser(String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan: " + username));

        List<LaporanHarian> laporanList = laporanHarianRepository.findByUserOrderByTanggalLaporanDesc(currentUser);

        return laporanList.stream()
                .map(this::mapToLaporanHarianResponse)
                .collect(Collectors.toList());
    }

    public List<LaporanHarianResponse> getAllLaporan() {
        return laporanHarianRepository.findAll().stream()
                .map(this::mapToLaporanHarianResponse)
                .collect(Collectors.toList());
    }

    public LaporanHarianResponse getLaporanById(Long id) {
        LaporanHarian laporan = laporanHarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laporan dengan ID " + id + " tidak ditemukan"));
        return mapToLaporanHarianResponse(laporan);
    }

    // Metode private untuk mengubah Entity menjadi DTO
    private LaporanHarianResponse mapToLaporanHarianResponse(LaporanHarian laporan) {
        List<RincianLaporanResponse> rincianDtoList = laporan.getRincianList().stream()
                .map(rincian -> RincianLaporanResponse.builder()
                        .rincianId(rincian.getRincianId())
                        .kategoriUtama(rincian.getKategoriUtama())
                        .kategoriSub(rincian.getKategoriSub())
                        .keterangan(rincian.getKeterangan())
                        .nilaiRupiah(rincian.getNilaiRupiah())
                        .nilaiKuantitas(rincian.getNilaiKuantitas())
                        .satuan(rincian.getSatuan())
                        .build())
                .collect(Collectors.toList());

        return LaporanHarianResponse.builder()
                .laporanId(laporan.getLaporanId())
                .submittedBy(laporan.getUser().getFullName())
                .divisi(laporan.getDivisi().name())
                .tanggalLaporan(laporan.getTanggalLaporan())
                .rincian(rincianDtoList)
                .build();
    }

    // --- METODE BARU UNTUK UPDATE ---
    @Transactional
    public void updateLaporan(Long laporanId, UpdateLaporanRequest request, String username) {
        // 1. Cari laporan yang ada di database berdasarkan ID
        LaporanHarian laporan = laporanHarianRepository.findById(laporanId)
                .orElseThrow(() -> new EntityNotFoundException("Laporan dengan ID " + laporanId + " tidak ditemukan"));

        // 2. Verifikasi Keamanan: Pastikan user yang login adalah pemilik laporan
        if (!laporan.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Anda tidak memiliki izin untuk mengedit laporan ini.");
        }

        // 3. Update data induknya (tanggal laporan)
        laporan.setTanggalLaporan(request.getTanggalLaporan());

        // 4. Update data rincian (anak-anaknya) dengan strategi "hapus dan buat ulang"
        // Ini cara paling aman dan sederhana untuk menangani penambahan, penghapusan, dan pengeditan rincian sekaligus.
        laporan.getRincianList().clear(); // Hapus semua rincian lama

        List<RincianLaporan> newRincianList = request.getRincian().stream()
                .map(dto -> RincianLaporan.builder()
                        .kategoriUtama(dto.getKategoriUtama())
                        .kategoriSub(dto.getKategoriSub())
                        .keterangan(dto.getKeterangan())
                        .nilaiRupiah(dto.getNilaiRupiah())
                        .nilaiKuantitas(dto.getNilaiKuantitas())
                        .satuan(dto.getSatuan())
                        .laporan(laporan)
                        .build())
                .collect(Collectors.toList());

        laporan.getRincianList().addAll(newRincianList); // Tambahkan semua rincian yang baru

        // 5. Simpan perubahan. JPA akan menangani semuanya.
        laporanHarianRepository.save(laporan);
    }

    // --- METODE BARU UNTUK DELETE ---
    @Transactional
    public void deleteLaporan(Long laporanId, String username) {
        // 1. Cari laporan yang ada
        LaporanHarian laporan = laporanHarianRepository.findById(laporanId)
                .orElseThrow(() -> new EntityNotFoundException("Laporan dengan ID " + laporanId + " tidak ditemukan"));

        // 2. Verifikasi Keamanan: Pastikan user yang login adalah pemilik laporan
        if (!laporan.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Anda tidak memiliki izin untuk menghapus laporan ini.");
        }

        // 3. Hapus laporan. Rinciannya akan ikut terhapus karena CascadeType.ALL
        laporanHarianRepository.delete(laporan);
    }
}