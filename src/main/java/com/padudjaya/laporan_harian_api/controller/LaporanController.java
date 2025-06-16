package com.padudjaya.laporan_harian_api.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padudjaya.laporan_harian_api.dto.LaporanHarianRequest;
import com.padudjaya.laporan_harian_api.dto.LaporanHarianResponse;
import com.padudjaya.laporan_harian_api.dto.UpdateLaporanRequest;
import com.padudjaya.laporan_harian_api.service.LaporanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/laporan") // Semua endpoint laporan akan berada di sini
@RequiredArgsConstructor
public class LaporanController {

    private final LaporanService laporanService;

    @PostMapping
    public ResponseEntity<String> createLaporan(
            @RequestBody LaporanHarianRequest request,
            Principal principal // Objek ini otomatis diisi oleh Spring Security dengan data user yang login
    ) {
        // Ambil username dari user yang sedang login
        String username = principal.getName();
        laporanService.createLaporan(request, username);
        return new ResponseEntity<>("Laporan berhasil dibuat", HttpStatus.CREATED);
    }

    // --- ENDPOINT-ENDPOINT GET BARU ---

    // Endpoint untuk mengambil semua laporan milik user yang sedang login
    @GetMapping("/saya")
    public ResponseEntity<List<LaporanHarianResponse>> getMyLaporan(Principal principal) {
        List<LaporanHarianResponse> response = laporanService.getAllLaporanForCurrentUser(principal.getName());
        return ResponseEntity.ok(response);
    }

    // Endpoint HANYA UNTUK SUPERADMIN untuk mengambil semua laporan dari semua user
    @GetMapping("/semua")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<List<LaporanHarianResponse>> getAllLaporan() {
        return ResponseEntity.ok(laporanService.getAllLaporan());
    }

    // Endpoint untuk mengambil satu laporan spesifik berdasarkan ID-nya
    @GetMapping("/{id}")
    public ResponseEntity<LaporanHarianResponse> getLaporanById(@PathVariable Long id) {
        return ResponseEntity.ok(laporanService.getLaporanById(id));
    }

    // --- ENDPOINT BARU UNTUK UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<String> updateLaporan(
            @PathVariable Long id,
            @RequestBody UpdateLaporanRequest request,
            Principal principal) {

        laporanService.updateLaporan(id, request, principal.getName());
        return ResponseEntity.ok("Laporan berhasil diperbarui.");
    }

    // --- ENDPOINT BARU UNTUK DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLaporan(@PathVariable Long id, Principal principal) {
        laporanService.deleteLaporan(id, principal.getName());
        return ResponseEntity.ok("Laporan berhasil dihapus.");
    }
}