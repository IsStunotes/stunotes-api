package com.example.config;

import com.example.security.JWTConfigurer;
import com.example.security.JWTFilter;
import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JWTFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // TODO: Permite solicitudes CORS desde otros dominios
                .csrf(AbstractHttpConfigurer::disable) // TODO: Desactiva la protección CSRF, ya que en APIs REST no se usa (se autentica con tokens, no con cookies)

                .authorizeHttpRequests(authorize -> authorize
                        // TODO: Permitir acceso público a las rutas de login, registro y endpoints públicos como Swagger UI
                        .requestMatchers(antMatcher("/auth/login")).permitAll()
                        .requestMatchers(antMatcher("/auth/register/teacher")).permitAll()
                        .requestMatchers(antMatcher("/auth/register/student")).permitAll()
                        .requestMatchers(antMatcher("/user/profile/{id}")).permitAll()
                        .requestMatchers(antMatcher("/tasks")).permitAll()
                        .requestMatchers(antMatcher("/tasks/{id}")).permitAll()
                        .requestMatchers(antMatcher("/categories")).permitAll()
                        .requestMatchers(antMatcher("/categories/page")).permitAll()
                        .requestMatchers(antMatcher("/categories/{id}")).permitAll()
                        .requestMatchers(antMatcher("/comments")).permitAll()
                        .requestMatchers(antMatcher("/comments/{id}")).permitAll()
                        .requestMatchers(antMatcher("/comments/document/{documentId}")).permitAll()
                        .requestMatchers(antMatcher("/comments/usuario/{usuarioId}")).permitAll()
                        .requestMatchers(antMatcher("/documents")).permitAll()
                        .requestMatchers(antMatcher("/documents/{id}")).permitAll()
                        .requestMatchers(antMatcher("/documents/{documentId}/comments")).permitAll()
                        .requestMatchers(antMatcher("/documents/repositorio/{repositorioId}")).permitAll()
                        .requestMatchers(antMatcher("/reminder")).permitAll()
                        .requestMatchers(antMatcher("/reminder/{id}")).permitAll()
                        .requestMatchers(antMatcher("/reminder/{id}/export-pdf")).permitAll()
                        .requestMatchers(antMatcher("/repositories")).permitAll()
                        .requestMatchers(antMatcher("/repositories/{id}")).permitAll()
                        .requestMatchers(antMatcher("/repositories/usuario/{usuarioId}")).permitAll()

                        .requestMatchers("/api/v1/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                        // TODO: Cualquier otra solicitud requiere autenticación (JWT u otra autenticación configurada)
                        .anyRequest().authenticated()
                )

                // TODO: Permite la autenticación básica (para testing con Postman, por ejemplo)
                .httpBasic(Customizer.withDefaults())
                // TODO: Desactiva el formulario de inicio de sesión predeterminado, ya que se usará JWT
                //.formLogin(AbstractHttpConfigurer::disable)
                // TODO: Configura el manejo de excepciones para autenticación. Usa JwtAuthenticationEntryPoint para manejar errores 401 (no autorizado)
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // TODO: Configura la política de sesiones como "sin estado" (stateless), ya que JWT maneja la autenticación, no las sesiones de servidor
                .sessionManagement(h -> h.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // TODO: Agrega la configuración para JWT en el filtro antes de los filtros predeterminados de Spring Security
                .with(new JWTConfigurer(tokenProvider), Customizer.withDefaults());

            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // TODO: Proporciona el AuthenticationManager que gestionará la autenticación basada en los detalles de usuario y contraseña
        return authenticationConfiguration.getAuthenticationManager();
    }
}
