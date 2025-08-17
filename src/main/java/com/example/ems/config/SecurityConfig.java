package com.example.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails employee = User.withUsername("employee")
                .password(encoder.encode("employee123"))
                .roles("EMPLOYEE")
                .build();

        UserDetails manager = User.withUsername("manager")
                .password(encoder.encode("manager123"))
                .roles("MANAGER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(employee, manager, admin);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/", "/index", "/css/**", "/js/**").permitAll()
                .requestMatchers("/search/department","/search/job").permitAll()

                .requestMatchers("/confirmDelete/**", "/deleteEmp/**", "/search/deleteEmp/**").hasRole("ADMIN")

                .requestMatchers("/loadEmpSave", "/saveEmp").hasAnyRole("MANAGER","ADMIN")
                .requestMatchers("/EditEmp/**", "/updateEmpDtls").hasAnyRole("MANAGER","ADMIN")
                .requestMatchers("/search/EditEmp/**").hasAnyRole("MANAGER","ADMIN")
                .requestMatchers("/report").hasAnyRole("MANAGER","ADMIN")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll())
            .exceptionHandling(exception -> 
                exception.accessDeniedPage("/403") // Redirect on 403
            );

        return http.build();
    }
}
