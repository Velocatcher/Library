package com.books.library.config;

import com.books.library.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Обновлено использование метода csrf
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/index", "/register", "/css/**", "/js/**").permitAll() // Доступ без авторизации
                        .requestMatchers("/home").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .formLogin((form) -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true) // Перенаправление на /home после входа
                                .successHandler(new CustomAuthenticationSuccessHandler())
                                .permitAll()
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .successHandler(new CustomAuthenticationSuccessHandler()) // Указан кастомный обработчик
//                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

