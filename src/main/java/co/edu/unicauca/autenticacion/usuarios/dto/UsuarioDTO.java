package co.edu.unicauca.autenticacion.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Integer oidUsuario;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String correo;
}
