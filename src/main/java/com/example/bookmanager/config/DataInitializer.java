package com.example.bookmanager.config;

import com.example.bookmanager.model.Role;
import com.example.bookmanager.model.User;
import com.example.bookmanager.repository.RoleRepository;
import com.example.bookmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = new Role("USER");
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role("ADMIN");
                    return roleRepository.save(role);
                });

        // Initialize test users
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setEmail("user@example.com");
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            System.out.println("✅ Tài khoản USER đã được tạo: user / password123");
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("✅ Tài khoản ADMIN đã được tạo: admin / admin123");
        }
    }
}
