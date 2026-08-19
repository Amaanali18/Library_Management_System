package com.amaan.backend.config;

import com.amaan.backend.entity.Role;
import com.amaan.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Seeder {
    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotExists(roleRepository,"USER");
            createRoleIfNotExists(roleRepository,"LIBRARIAN");
            createRoleIfNotExists(roleRepository,"ADMIN");
        };
    }

    private void createRoleIfNotExists(RoleRepository roleRepository, String user) {
        if(!roleRepository.existsByName(user)) {
            Role role = Role.builder().name(user).build();
            roleRepository.save(role);
        }
    }
}
