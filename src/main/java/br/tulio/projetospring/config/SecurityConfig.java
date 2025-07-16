package br.tulio.projetospring.config;

import br.tulio.projetospring.security.jwt.JwtTokenFilter;
import br.tulio.projetospring.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        //monta o codificador com um tamanho de 8bytes e executa 185000 iterações da função de hash PBKDF2WithHmacSHA256
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        //Monta o mapa com codificadores personalizados
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders); //util para migrações de algoritmo e suporte a multiplos tipos de codificação


        passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("pbkdf2")); //define o pdkdf2 como padrão
        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);

        return http
                .httpBasic(AbstractHttpConfigurer::disable) //desativa a autenticação basica (login padrão com pop-up )
                .csrf(AbstractHttpConfigurer::disable)  //desativa a proteção controa CSRF, pois a API é stateless
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class) //adiciona nosso filtro
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(  //define endpoints sem autenticação
                                        "/auth/signin",
                                        "/auth/refresh/**",
                                        "/auth/createUser", //endpoint temporario pra aprender (DEVE SER REMOVIDO EM PROJETOS)
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers("/users").denyAll()  //endpoints sempre negados
                                .anyRequest().authenticated() //todos os outros endpoints pedem autenticação
                )
                .cors(cors -> {}) //ativa o suporte a CORS com tudo padrão
                .build();
    }



}
