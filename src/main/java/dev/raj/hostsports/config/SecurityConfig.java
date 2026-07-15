package dev.raj.hostsports.config;


import dev.raj.hostsports.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/h2-console/**",
            "/ws/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()


                        .requestMatchers("GET", "/api/venues/mine").hasAnyRole("VENUE_OWNER", "ADMIN")
                        .requestMatchers("GET", "/api/venues/**").permitAll()
                        .requestMatchers("GET", "/api/slots/**").permitAll()
                        .requestMatchers("/api/venues/**").hasAnyRole("VENUE_OWNER", "ADMIN")
                        .requestMatchers("/api/slots/**").hasAnyRole("VENUE_OWNER", "ADMIN")
                        .requestMatchers("/api/bookings/**").hasAnyRole("PLAYER", "ADMIN")
                        .requestMatchers("/api/payments/**").hasAnyRole("PLAYER", "ADMIN")


                        .requestMatchers("GET", "/api/tournaments/mine").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers("GET", "/api/tournaments/**").permitAll()
                        .requestMatchers("/api/tournaments/**").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers("GET", "/api/teams/mine").hasAnyRole("TEAM_CAPTAIN", "ADMIN")
                        .requestMatchers("GET", "/api/teams/**").permitAll()
                        .requestMatchers("/api/teams/**").hasAnyRole("TEAM_CAPTAIN", "ADMIN")
                        .requestMatchers("/api/registrations/**").hasAnyRole("TEAM_CAPTAIN", "ORGANIZER", "ADMIN")
                        .requestMatchers("GET", "/api/matches/**").permitAll()
                        .requestMatchers("/api/matches/**").hasAnyRole("ORGANIZER", "ADMIN")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Needed only so the H2 console (served in frames) still renders during local dev
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
