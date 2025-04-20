package co.edu.unicauca.autenticacion.usuarios.service.impl;

import co.edu.unicauca.autenticacion.usuarios.common.util.KiraUtil;
import co.edu.unicauca.autenticacion.usuarios.dto.KiraResponseDTO;
import co.edu.unicauca.autenticacion.usuarios.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String userId, KiraResponseDTO kiraUserInfo, String tokenOriginal, String username) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("nombres", KiraUtil.obtenerNombres(kiraUserInfo))
                .claim("apellidos", KiraUtil.obtenerApellidos(kiraUserInfo))
                .claim("correo", kiraUserInfo.getCorreo())
                .claim("telefono", kiraUserInfo.getCelular())
                .claim("codigoAcademico", KiraUtil.obtenerCodigoPrograma(kiraUserInfo))
                .claim("tipoIdentificacion", kiraUserInfo.getTipoIdentificacion())
                .claim("numeroIdentificacion", kiraUserInfo.getIdentificacion())
                .claim("rol", KiraUtil.obtenerRolPrograma(kiraUserInfo))
                .claim("username", username)
                .claim("originalToken", tokenOriginal)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
