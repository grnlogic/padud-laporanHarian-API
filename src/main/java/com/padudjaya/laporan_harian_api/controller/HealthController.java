package com.padudjaya.laporan_harian_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
    allowCredentials = "true",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.OPTIONS}
)
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // Status aplikasi
            status.put("application", "UP");
            status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            status.put("version", "1.0.0");
            
            // Status database
            Map<String, Object> database = checkDatabaseStatus();
            status.put("database", database);
            
            // Status keseluruhan
            boolean isHealthy = "UP".equals(database.get("status"));
            status.put("status", isHealthy ? "UP" : "DOWN");
            
            return ResponseEntity.ok(status);
            
        } catch (Exception e) {
            status.put("application", "DOWN");
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
            return ResponseEntity.status(503).body(status);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        Map<String, Object> dbStatus = checkDatabaseStatus();
        
        if ("UP".equals(dbStatus.get("status"))) {
            return ResponseEntity.ok(dbStatus);
        } else {
            return ResponseEntity.status(503).body(dbStatus);
        }
    }

    private Map<String, Object> checkDatabaseStatus() {
        Map<String, Object> dbStatus = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            // Test koneksi database
            boolean isValid = connection.isValid(5); // timeout 5 detik
            
            if (isValid) {
                dbStatus.put("status", "UP");
                dbStatus.put("database", connection.getMetaData().getDatabaseProductName());
                dbStatus.put("version", connection.getMetaData().getDatabaseProductVersion());
                dbStatus.put("url", sanitizeUrl(connection.getMetaData().getURL()));
                dbStatus.put("driver", connection.getMetaData().getDriverName());
                
                // Test query sederhana
                try (var stmt = connection.createStatement()) {
                    var rs = stmt.executeQuery("SELECT 1");
                    dbStatus.put("query_test", rs.next() ? "PASS" : "FAIL");
                }
                
            } else {
                dbStatus.put("status", "DOWN");
                dbStatus.put("error", "Database connection is not valid");
            }
            
        } catch (Exception e) {
            dbStatus.put("status", "DOWN");
            dbStatus.put("error", e.getMessage());
            dbStatus.put("error_type", e.getClass().getSimpleName());
        }
        
        dbStatus.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dbStatus;
    }

    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> getDetailedStatus() {
        Map<String, Object> detailed = new HashMap<>();
        
        // Informasi sistem
        Map<String, Object> system = new HashMap<>();
        system.put("java_version", System.getProperty("java.version"));
        system.put("os_name", System.getProperty("os.name"));
        system.put("os_version", System.getProperty("os.version"));
        system.put("available_processors", Runtime.getRuntime().availableProcessors());
        system.put("max_memory", formatMemory(Runtime.getRuntime().maxMemory()));
        system.put("total_memory", formatMemory(Runtime.getRuntime().totalMemory()));
        system.put("free_memory", formatMemory(Runtime.getRuntime().freeMemory()));
        system.put("used_memory", formatMemory(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        
        detailed.put("system", system);
        detailed.put("database", checkDatabaseStatus());
        detailed.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return ResponseEntity.ok(detailed);
    }

    // Helper method untuk format memory
    private String formatMemory(long bytes) {
        long mb = bytes / (1024 * 1024);
        return mb + " MB";
    }

    // Helper method untuk menyembunyikan password dari URL database
    private String sanitizeUrl(String url) {
        if (url != null && url.contains("password=")) {
            return url.replaceAll("password=[^&]*", "password=***");
        }
        return url;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApplicationInfo() {
        Map<String, Object> info = new HashMap<>();
        
        info.put("application_name", "Laporan Harian API");
        info.put("version", "1.0.0");
        info.put("description", "Backend API untuk sistem laporan harian Padud Jaya");
        info.put("build_time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        info.put("java_version", System.getProperty("java.version"));
        info.put("spring_boot_version", org.springframework.boot.SpringBootVersion.getVersion());
        
        return ResponseEntity.ok(info);
    }
}