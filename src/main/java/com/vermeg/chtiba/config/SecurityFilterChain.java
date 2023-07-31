package com.vermeg.chtiba.config;

import java.util.Arrays;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityFilterChain extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public SecurityFilterChain(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity)((HttpSecurity)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((HttpSecurity)((HttpSecurity)http
                .csrf()
                .disable())
                .cors()
                .configurationSource(corsConfigurationSource())

                .and())
                .authorizeHttpRequests()
                .antMatchers(new String[] { "/postman/**", "/auth/**", "/auth/login", "/auth/register", "/auth/logout/{id}" })).permitAll()
                .antMatchers(new String[] { "/auth/validate-token" })).permitAll()
                .anyRequest())
                .authenticated()
                .and())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and())
                .authenticationProvider(this.authenticationProvider)
                .addFilterBefore((Filter)this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(new String[] { "http://localhost:4200" }));
        configuration.setAllowedMethods(Arrays.asList(new String[] { "GET", "POST", "PUT", "DELETE", "OPTIONS" }));
        configuration.setAllowedHeaders(Arrays.asList(new String[] { "Authorization", "Content-Type" }));
        configuration.setExposedHeaders(Arrays.asList(new String[] { "Authorization" }));
        configuration.setAllowCredentials(Boolean.valueOf(true));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return (CorsConfigurationSource)source;
    }
}
