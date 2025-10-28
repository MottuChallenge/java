package br.com.mottugrid_java.infrastructure.config;

import br.com.mottugrid_java.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("SELECT username, password, 1 FROM app_user WHERE username = ?");
        users.setAuthoritiesByUsernameQuery("SELECT username, role FROM app_user WHERE username = ?");
        return users;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 1. Cadeia de Filtros para a API (Stateless/JWT) - Ordem 1 (Prioritária)
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated()
                )
                // Adiciona o filtro JWT customizado
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. Cadeia de Filtros para a Web (Stateful/Form Login) - Padrão
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 💡 IMPORTANTE: Remove o filtro JWT desta cadeia, pois ele não deve processar o login com formulário.
                // O filtro é injetado automaticamente como Bean, então precisamos ter certeza de que ele NÃO afete o fluxo WEB, mas isso é tratado pelo securityMatcher.
                // Vamos focar em garantir que as rotas de login sejam permitidas.
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso a recursos estáticos e login page SEM AUTENTICAÇÃO
                        .requestMatchers("/css/**", "/swagger-ui/**", "/v3/api-docs/**", "/login").permitAll()
                        // Rotas de dashboard e outras: Requerem autenticação
                        .requestMatchers("/", "/dashboard").authenticated()
                        // Restrição por Role para rotas de exclusão (Web)
                        .requestMatchers("/branches/delete/**", "/motorcycles/delete/**", "/yards/delete/**").hasRole("ADMIN")
                        // Qualquer outra requisição requer autenticação
                        .anyRequest().authenticated()
                )
                // Configuração de Login por Formulário (para a interface web)
                .formLogin(form -> form
                        .loginPage("/login")
                        // Após sucesso, redireciona para a raiz (Dashboard), que agora está protegida
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Configuração de Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}