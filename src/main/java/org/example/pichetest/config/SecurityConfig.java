package org.example.pichetest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(HttpMethod.POST, "/register").permitAll()
            .requestMatchers(HttpMethod.POST, "/accounts").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/accounts").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/details/**").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/transactions/deposit").permitAll()
            .requestMatchers(HttpMethod.POST, "/transactions/withdraw").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/transactions/transfer").hasRole("USER")
            .requestMatchers("/hello").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .permitAll()
            .and()
            .httpBasic()
            .and()
            .csrf().disable();
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
