package br.com.mottugrid_java.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;


    /**
     * Injeção via Construtor.
     * @param jwtTokenUtil Componente utilitário para JWT.
     * @param userDetailsService Serviço para carregar detalhes do usuário.
     */
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Executa a lógica de filtro para cada requisição HTTP.
     * 1. Extrai o token do header Authorization.
     * 2. Valida o token e carrega o UserDetails.
     * 3. Configura a autenticação no contexto do Spring Security se o token for válido.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 1. Extrai o JWT Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                // Em produção, isso deve ser logado como WARN/ERROR, não impresso diretamente.
                logger.debug("JWT Token inválido ou expirado. " + e.getMessage());
            }
        }

        // 2. Valida o token e configura o SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega os detalhes do usuário (incluindo authorities)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Se o token for válido, cria o objeto de autenticação
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define o usuário atual como autenticado no Spring Security
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
