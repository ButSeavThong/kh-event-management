package com.thong.event.feature.admin.controller;

import com.thong.event.feature.admin.DashboardService;
import com.thong.event.feature.admin.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * ADMIN: Get dashboard statistics
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse dashboard = dashboardService.getDashboardStats();
        return ResponseEntity.ok(dashboard);
    }
}