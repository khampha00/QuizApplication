package com.skillup.edtech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/register", "/css/**", "/js/**").permitAll() // Allow access to registration [cite: 99]
                        .requestMatchers("/quiz-list", "/add-quiz", "/edit-quiz/**", "/delete-quiz/**").hasRole("ADMIN") // Admin pages [cite: 100]
                        .requestMatchers("/quiz", "/submit", "/result").hasRole("USER") // User pages [cite: 101]
                        .anyRequest().authenticated() // All other requests need authentication [cite: 102]
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(roleBasedSuccessHandler()) // Custom handler for role-based redirection
                        .permitAll()
                )
                // ... previous configuration
                .logout(logout -> logout
                        // .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // <-- Deprecated line
                        .logoutUrl("/logout") // <-- Corrected line
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
// ...

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                // Redirect user based on their role after successful login [cite: 127]
                boolean isAdmin = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch("ROLE_ADMIN"::equals);

                if (isAdmin) {
                    response.sendRedirect("/quiz-list");
                } else {
                    response.sendRedirect("/quiz");
                }
            }
        };
    }
}