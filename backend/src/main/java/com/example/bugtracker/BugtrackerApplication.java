package com.example.bugtracker;

import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class BugtrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BugtrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ADMIN, Role.USER));
                userRepository.save(admin);
            }
        };
    }
}
