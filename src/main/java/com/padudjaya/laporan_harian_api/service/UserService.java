// File: UserService.java
package com.padudjaya.laporan_harian_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padudjaya.laporan_harian_api.dto.UpdateUserRequest;
import com.padudjaya.laporan_harian_api.dto.UserResponse;
import com.padudjaya.laporan_harian_api.model.User;
import com.padudjaya.laporan_harian_api.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan"));

        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        
        // Update password jika disediakan
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan");
        }
        userRepository.deleteById(userId);
    }

    // Metode private untuk mengubah User Entity menjadi UserResponse DTO
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}