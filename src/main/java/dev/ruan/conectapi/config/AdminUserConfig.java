package dev.ruan.conectapi.config;

import dev.ruan.conectapi.entities.Role;
import dev.ruan.conectapi.entities.User;
import dev.ruan.conectapi.repository.RoleRepository;
import dev.ruan.conectapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByEmail("admin@gmail.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe!");
                },
                () -> {
                    var user = new User();
                    user.setEmail("admin@gmail.com");
                    user.setPassword(passwordEncoder.encode("Fo9QTgUlJH8mEDFE"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}