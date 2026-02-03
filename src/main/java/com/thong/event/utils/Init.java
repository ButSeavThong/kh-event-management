package com.thong.event.utils;

import com.thong.event.domain.Role;
import com.thong.event.feature.role.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Init {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            Set<Role> roles = new HashSet<>();

            Role user = new Role();
            user.setName("USER");

            Role admin = new Role();
            admin.setName("ADMIN");


            roles.add(user);
            roles.add(admin);
            roleRepository.saveAll(roles);

        }
    }

}