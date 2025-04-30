package br.com.cdb.bancodigital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable) // desabilita proteção CSRF (ok em dev)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite todos os endpoints sem login
                )
                .httpBasic(Customizer.withDefaults()); // ou .formLogin().disable() se quiser remover totalmente o login

        return http.build();
    }
}
