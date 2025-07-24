package com.check.user_check.config.security;

import com.check.user_check.enumeratedType.Role;
import com.check.user_check.config.security.filter.JwtAuthenticationFilter;
import com.check.user_check.config.security.filter.LoginFilter;
import com.check.user_check.config.security.filter.RefreshTokenFilter;
import com.check.user_check.config.security.handler.AuthenticationEntryPointHandler;
import com.check.user_check.config.security.handler.CustomAccessDeniedHandler;
import com.check.user_check.config.security.handler.LoginFailureHandler;
import com.check.user_check.config.security.handler.LoginSuccessHandler;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.service.response.basic.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JWTUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    @Value("${access-token-expiration-time}")
    private int accessTokenExpirationTime;

    @Value("${refresh-token-expiration-time}")
    private int refreshTokenExpirationTime;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        LoginFilter loginFilter = new LoginFilter("/api/user/auth/login");
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler(
                jwtUtil, accessTokenExpirationTime, refreshTokenExpirationTime, refreshTokenService);
        LoginFailureHandler loginFailureHandler = new LoginFailureHandler();

        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler);

        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .formLogin(loginForm -> loginForm.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/auth/**",
                                "/docs/**", "/"
                                , "/index.html", "/assets/**", "/api/auth/validate-token"
                                , "/api/user/auth/refresh"
                                , "/admin/**", "/user/**"   //front 페이지 url
                                , "/js/**", "/css/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(corsCustom -> corsCustom.configurationSource(corsConfigurationSource()))
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPointHandler)
                )
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenFilter(
                        "/api/user/auth/refresh",
                                jwtUtil, refreshTokenExpirationTime, accessTokenExpirationTime,
                                refreshTokenService)
                        , JwtAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){

        Role[] roles = Role.values();
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < roles.length - 1; i++) {
            stringBuilder.append(roles[i].name() + " > " + roles[i + 1].name() + "\n");
        }

        return RoleHierarchyImpl.fromHierarchy(stringBuilder.toString());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173", "http://192.168.0.8:8081", "https://kimjr.shop:9443"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }
}
