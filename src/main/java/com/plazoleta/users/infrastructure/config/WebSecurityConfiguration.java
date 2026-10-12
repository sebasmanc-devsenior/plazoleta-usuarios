package com.plazoleta.users.infrastructure.config;

import com.plazoleta.users.infrastructure.security.ApiSecurityErrorHandlers;
import com.plazoleta.users.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration {

    /** HU-5: cada endpoint exige el rol que le corresponde; lo no declarado se rechaza. */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter,
                                            ApiSecurityErrorHandlers errorHandlers) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        // HU-1 + HU-5: solo el administrador crea propietarios.
                        .requestMatchers("/api/owners").hasRole("ADMIN")
                        // HU-6 + HU-5: solo un propietario crea empleados (el dueño se valida en el caso de uso).
                        .requestMatchers("/api/employees").hasRole("PROPIETARIO")
                        // Rutas entre microservicios: el controlador valida el token interno.
                        .requestMatchers("/internal/**").permitAll()
                        .anyRequest().denyAll())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(errorHandlers)
                        .accessDeniedHandler(errorHandlers))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
