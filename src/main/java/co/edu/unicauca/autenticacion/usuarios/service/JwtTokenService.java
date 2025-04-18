package co.edu.unicauca.autenticacion.usuarios.service;

import co.edu.unicauca.autenticacion.usuarios.dto.KiraResponseDTO;

public interface JwtTokenService {
    
    String generateToken(String userId, KiraResponseDTO kiraUserInfo, String tokenOriginal, String username);
}
