package com.raghav.expense.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.raghav.expense.filter.JwtFilter;
import com.raghav.expense.service.UserDetailsServiceImpl;

@Configuration 
@EnableWebSecurity 
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter)
    {
        this.jwtFilter=jwtFilter;
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            request ->
            request.requestMatchers("/auth/register","/auth/login").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean 
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userService,PasswordEncoder passwordEncoder)
    {
        DaoAuthenticationProvider d=new DaoAuthenticationProvider(userService);
        d.setPasswordEncoder(passwordEncoder);
        return d;
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }
}
