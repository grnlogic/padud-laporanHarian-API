package com.padudjaya.laporan_harian_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.padudjaya.laporan_harian_api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA akan otomatis membuat query untuk mencari user berdasarkan username
    // karena nama method-nya findByUsername
    Optional<User> findByUsername(String username);
}