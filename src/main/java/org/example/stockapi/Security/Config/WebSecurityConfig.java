package org.example.stockapi.Security.Config;

import org.example.stockapi.Security.Jwt.AuthEntryPointJwt;
import org.example.stockapi.Security.Jwt.AuthTokenFilter;
import org.example.stockapi.Security.Service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2SuccessHandler customOAuth2SuccessHandler, AuthTokenFilter authTokenFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/stock/getStocks").permitAll()
                .requestMatchers("/stock/getTicker").permitAll()
                .requestMatchers("/currency/test").permitAll()
                .requestMatchers("/auth/google").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
                .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationJwtTokenFilter()))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/google").userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler));
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthEntryPointJwt authenticationJwtTokenFilter() {
        return new AuthEntryPointJwt();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoderConfig.passwordEncoder());
        return provider;
    }
}
