package com.vermeg.chtiba.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vermeg.chtiba.entities.Client;
import com.vermeg.chtiba.repositories.ClientRepository;

@Configuration
@EnableWebSecurity
@Order(2)
public class appConfig extends WebSecurityConfigurerAdapter {
    private final ClientRepository UR;

    public appConfig(ClientRepository UR) {
        this.UR = UR;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Client user = this.UR.findByEmail(username).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));
            return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
        };
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return (AuthenticationProvider)authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return (PasswordEncoder)new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public Client getUser() {
        return new Client();
    }
}
