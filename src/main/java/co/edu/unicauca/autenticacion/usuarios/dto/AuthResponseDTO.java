package co.edu.unicauca.autenticacion.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UsuarioDTO usuario;
}

