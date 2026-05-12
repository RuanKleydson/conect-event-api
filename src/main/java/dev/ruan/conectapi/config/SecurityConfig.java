package dev.ruan.conectapi.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public SecurityConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/documents/template").hasAnyAuthority("ROLE_BASIC", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/documents/submissions").hasAuthority("ROLE_BASIC")
                        .requestMatchers(HttpMethod.PUT, "/documents/submissions/*/approve").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthConverter() {

        return jwt -> {

            List<String> roles = jwt.getClaimAsStringList("roles");

            if (roles == null) {
                roles = List.of();
            }

            // Adicionar prefixo ROLE_ para compatibilidade com hasAuthority()
            var authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
