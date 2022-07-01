package br.ufrn.imd.security;

import br.ufrn.imd.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserRepository repository;

    SecurityConfig(UserRepository repository) {
        this.repository = repository;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().disable().csrf().disable()
                .authorizeHttpRequests(registry ->
                        registry.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .antMatchers(HttpMethod.POST, "/login", "/users").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(new JwtLoginFilter("/login", new NaiveAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    private class NaiveAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            var users = repository.findByEmail(authentication.getName());
            if (users == null || users.isEmpty()) {
                throw new BadCredentialsException("Invalid username or password");
            }
            var user = users.get(0);
            if (!user.getPassword().equals(authentication.getCredentials())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            return authentication;
        }
    }
}

