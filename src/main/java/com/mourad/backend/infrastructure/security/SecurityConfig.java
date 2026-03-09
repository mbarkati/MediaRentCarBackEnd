package com.mourad.backend.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mourad.backend.domain.port.out.TokenPort;
import com.mourad.backend.interfaces.dto.response.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenPort tokenPort;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    public SecurityConfig(TokenPort tokenPort) {
        this.tokenPort = tokenPort;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        if ("*".equals(allowedOrigins)) {
            config.addAllowedOriginPattern("*");
        } else {
            for (String origin : allowedOrigins.split(",")) {
                config.addAllowedOrigin(origin.trim());
            }
        }
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(!"*".equals(allowedOrigins));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(tokenPort, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                    JwtAuthenticationFilter jwtAuthFilter,
                                                    ObjectMapper objectMapper)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Allow H2 frames (dev console only — disabled in prod)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((req, resp, ex) -> {
                            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ApiError error = ApiError.of(
                                    HttpStatus.UNAUTHORIZED, "Authentication required", req.getRequestURI());
                            resp.getWriter().write(objectMapper.writeValueAsString(error));
                        })
                        .accessDeniedHandler((req, resp, ex) -> {
                            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            ApiError error = ApiError.of(
                                    HttpStatus.FORBIDDEN, "Access denied", req.getRequestURI());
                            resp.getWriter().write(objectMapper.writeValueAsString(error));
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Public car browsing (mobile / client)
                        .requestMatchers("/api/cars/**").permitAll()
                        // Public city list (mobile reads at startup)
                        .requestMatchers("/api/cities/**").permitAll()
                        // Public app config (mobile reads at startup)
                        .requestMatchers("/api/config/**").permitAll()
                        // Swagger UI
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // H2 console (only reachable when spring.h2.console.enabled=true in dev)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Web form for account deletion (Google Play policy — no app required)
                        .requestMatchers("/delete-account").permitAll()
                        // Mobile user self-service: delete own account via JWT
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me").hasRole("APP_USER")
                        // All admin routes require ADMIN role
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
