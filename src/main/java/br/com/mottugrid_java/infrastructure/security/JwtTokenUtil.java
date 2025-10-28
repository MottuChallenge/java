package br.com.mottugrid_java.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // Injeta a chave secreta e o tempo de expiração do application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // Em milissegundos

    /**
     * Gera a chave de assinatura (Key) a partir do secret em Base64.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // -----------------------------------------------------------
    // GERAÇÃO DO TOKEN
    // -----------------------------------------------------------

    /**
     * Gera um JWT para o usuário. Adiciona o 'role' como um claim personalizado.
     * @param userDetails Detalhes do usuário autenticado.
     * @return O JWT token como String.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Adicionando o papel (role) do usuário como um "claim"
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -----------------------------------------------------------
    // VALIDAÇÃO E EXTRAÇÃO
    // -----------------------------------------------------------

    /**
     * Valida o token comparando o username e verificando a expiração.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrai o username (Subject) do token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Verifica se o token expirou.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Extrai a data de expiração do token.
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Método genérico para obter qualquer claim do token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims;
        try {
            claims = getAllClaimsFromToken(token);
        } catch (Exception e) {
            // Em caso de token inválido/expirado, retorna null
            return null;
        }
        return claimsResolver.apply(claims);
    }

    /**
     * Converte o token JWT em um objeto Claims (corpo).
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
