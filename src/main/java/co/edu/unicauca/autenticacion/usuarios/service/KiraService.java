package co.edu.unicauca.autenticacion.usuarios.service;

import co.edu.unicauca.autenticacion.usuarios.dto.KiraResponseDTO;

public interface KiraService {
    
    KiraResponseDTO getUserInfo(String username);
}
