package com.example.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.example.security.JWTConfigurer;
import com.example.security.JWTFilter;
import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.TokenProvider;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
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
                        .requestMatchers(antMatcher("/mail/**")).permitAll()
                        .requestMatchers(antMatcher("/chat")).permitAll()
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
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://stunotes-web.netlify.app/"));  // 🔁 Angular
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // métodos permitidos
        config.setAllowedHeaders(List.of("*")); // cualquier cabecera (como Authorization)
        config.setAllowCredentials(true); // necesario si usas JWT o cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // se aplica a todos los endpoints

        return new CorsFilter(source); // este filtro se activa automáticamente
    }
}
