package co.edu.unicauca.autenticacion.usuarios.controller;

import co.edu.unicauca.autenticacion.usuarios.dto.GoogleTokenRequest;
import co.edu.unicauca.autenticacion.usuarios.dto.JwtResponse;
import co.edu.unicauca.autenticacion.usuarios.dto.KiraResponseDTO;
import co.edu.unicauca.autenticacion.usuarios.dto.DataAdicionalKira;
import co.edu.unicauca.autenticacion.usuarios.service.JwtTokenService;
import co.edu.unicauca.autenticacion.usuarios.security.JwtUtil;
import co.edu.unicauca.autenticacion.usuarios.service.GoogleAuthService;
import co.edu.unicauca.autenticacion.usuarios.service.KiraService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final JwtTokenService jwtTokenService;
    private final KiraService kiraService;
    private final JwtUtil jwtUtil;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest googleTokenRequest) {
        // Validar el idToken de Google
        GoogleIdToken.Payload payload = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

        String email = payload.getEmail();
        String nombreUsuario = email.split("@")[0];

        //KiraResponseDTO kiraUserInfo = kiraService.getUserInfo(email);

        // Obtener información del usuario desde KIRA o provisional
        KiraResponseDTO kiraUserInfo = obtenerInfoProvisional(
                123L, "usuario123", 1, "CC",
                "123456789", "Perez", "Gomez",
                "Juan", "Carlos", email,
                "3001234567");

        // 💡 Establecer el rol. Puede venir de kiraUserInfo en un futuro.
        String rol = "ESTUDIANTE";

        // ✅ Generar token
        String jwtTokenBearer = jwtUtil.generateJwtToken(nombreUsuario, rol);

        // Puedes seguir generando un token adicional si es necesario
        String jwtToken = jwtTokenService.generateToken(
                UUID.randomUUID().toString(),
                kiraUserInfo,
                jwtTokenBearer,
                nombreUsuario);
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    private KiraResponseDTO obtenerInfoProvisional(Long oidTercero, String usuario, Integer oidTipoIdentificacion,
            String tipoIdentificacion, String identificacion,
            String primerApellido, String segundoApellido,
            String primerNombre, String segundoNombre,
            String correo, String celular) {

        List<DataAdicionalKira> lista = new ArrayList<>();
        lista.add(new DataAdicionalKira("Estudiante", "38", "12345678", "Ingeniería de Sistemas", "ACTIVO"));

        return new KiraResponseDTO(oidTercero, usuario, oidTipoIdentificacion, tipoIdentificacion,
                identificacion, primerApellido, segundoApellido, primerNombre,
                segundoNombre, correo, celular, lista);
    }
}
