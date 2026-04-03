package com.digitalfir.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	http
        .cors().and()
        .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/login").permitAll()

                .requestMatchers(
                        "/api/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers("/uploads/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/evidence/view/**").permitAll()

                .requestMatchers(
                        "/api/evidence/upload/**",
                        "/api/evidence/fir/**",
                        "/api/evidence/admin/view/**",
                        "/api/evidence/**"
                ).hasAnyAuthority("ROLE_CITIZEN", "ROLE_POLICE", "ROLE_ADMIN")

                .requestMatchers("/notifications/**")
                .hasAnyRole("CITIZEN", "POLICE", "ADMIN")

                .requestMatchers("/api/admin/**")
                .hasAuthority("ROLE_ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/police/profile")
                .hasAuthority("ROLE_POLICE")

                .requestMatchers(HttpMethod.GET, "/api/police/profile/me")
                .hasAnyAuthority("ROLE_POLICE", "ROLE_ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/fir/create")
                .hasAuthority("ROLE_CITIZEN")

                .requestMatchers(HttpMethod.PUT, "/api/fir/update-status/**")
                .hasAnyAuthority("ROLE_POLICE", "ROLE_ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/fir/**")
                .hasAnyAuthority("ROLE_CITIZEN", "ROLE_POLICE", "ROLE_ADMIN")

                .anyRequest().authenticated()
            )

            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}