package co.edu.unicauca.autenticacion.usuarios.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    
    public String generateJwtToken(String username, String rol) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        
        return token;
    }
    
    

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRolFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    public Collection<SimpleGrantedAuthority> getRolesFromJwtToken(String token) {
        String rol = getRolFromJwtToken(token);
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("🔐 Firma inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("❌ Token mal formado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("⏰ Token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("🚫 Token no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("⚠️ Token vacío: {}", e.getMessage());
        }
        return false;
    }
}
