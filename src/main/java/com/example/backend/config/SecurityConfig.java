package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Value("${dev.security.user.password}")
    private String devPassword;

    @Value("${test.security.user.password}")
    private String testPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/products/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Profile("dev")
    public UserDetailsService devUsers(PasswordEncoder passwordEncoder) {
        System.out.println("Loaded DEV user: devuser");
        return new InMemoryUserDetailsManager(
            User.withUsername("devuser")
                .password(passwordEndcoder().encode(devPassword))
                .roles("USER")
                .build()
        );
    }

    @Bean
    @Profile("test")
    public UserDetailsService testUsers(PasswordEncoder passwordEncoder) {
        System.out.println("Loaded TEST user: testuser");
        return new InMemoryUserDetailsManager(
            User.withUsername("testuser")
                .password(passwordEndcoder().encode(testPassword))
                .roles("USER")
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEndcoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
