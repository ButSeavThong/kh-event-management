package com.thong.event.utils;

import com.thong.event.domain.Role;
import com.thong.event.domain.User;
import com.thong.event.feature.role.RoleRepository;
import com.thong.event.feature.user.UserRespository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Init {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRespository userRespository;

    @PostConstruct
    public void init() {
        initRoles();
        initAdminUsers();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {

            Role user = new Role();
            user.setName("USER");

            Role admin = new Role();
            admin.setName("ADMIN");

            roleRepository.saveAll(Set.of(user, admin));
        }
    }

    private void initAdminUsers() {

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        createAdminIfNotExists(
                "ThongFazon",
                "thongfazon@gmail.com",
                "admin123",
                adminRole,
                userRole
        );

        createAdminIfNotExists(
                "tepy",
                "tepy@gmail.com",
                "admin123",
                adminRole,
                userRole
        );
    }

    private void createAdminIfNotExists(
            String username,
            String email,
            String rawPassword,
            Role adminRole,
            Role userRole
    ) {

        if (userRespository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        //  REQUIRED FIELDS (VERY IMPORTANT)
        user.setDob(LocalDate.of(2000, 1, 1));
        user.setGender("MALE"); // or enum if you use enum

        user.setIsDeleted(false);
        user.setIsBlocked(false);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(userRole);
        user.setRoles(roles);

        userRespository.save(user);
    }

}
