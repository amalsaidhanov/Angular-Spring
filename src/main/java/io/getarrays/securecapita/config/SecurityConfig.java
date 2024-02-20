package io.getarrays.securecapita.config;

import io.getarrays.securecapita.filter.CustomerFilter;
import io.getarrays.securecapita.handler.CustomAccessDeniedHandler;
import io.getarrays.securecapita.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity()
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {"/user/login/**","/user/register/**","/user/getall/**"};
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    //We can have many (for admins,users...)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(configure -> configure.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(PUBLIC_URLS).permitAll()
                                .requestMatchers(OPTIONS).permitAll()
                                .requestMatchers(DELETE, "/user/delete/**")
                                .hasAnyAuthority("DELETE:USER")
                                .requestMatchers(DELETE, "/customer/delete/**")
                                .hasAnyAuthority("DELETE:CUSTOMER")
                                .anyRequest().authenticated());
//                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(configure -> configure.configurationSource(corsConfigurationSource()));
//        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
//        http.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_URLS) - permitAlLO);
//        http.authorizeHttpRequests(request -> request.requestMatchers(OPTIONS) - permitAlLO); // Not needed
//        http.authorizeHttpRequests(request -> request.requestMatchers(DELETE, © "/user/delete/**").hasAnyAuthority(...
//        authorities:
//        "DELETE:USER"));
//        http.authorizeHttpRequests(request -> request.requestMatchers(DELETE, © "/customer/delete/**").hasAnyAuthority(..authorities:
//        "DELETE: CUSTOMER"));
//        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
//        http.authorizeHttpRequests(request -> request.anyRequest.authenticated);
//        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build;
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "http://securecapita.org", "http://192.168.1.164", "http://192.168.1.216", "http://100.14.212.97:5000"));
        //corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin", "Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "File-Name"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}














   /* Documentations
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationManager.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationProvider.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/DaoAuthenticationProvider.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/AbstractUserDetailsAuthenticationProvider.html
    */
