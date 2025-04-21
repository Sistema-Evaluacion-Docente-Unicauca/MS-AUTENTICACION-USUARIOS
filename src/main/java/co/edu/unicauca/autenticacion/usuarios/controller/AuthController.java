package co.edu.unicauca.autenticacion.usuarios.controller;

import co.edu.unicauca.autenticacion.usuarios.dto.GoogleTokenRequest;
import co.edu.unicauca.autenticacion.usuarios.dto.JwtResponse;
import co.edu.unicauca.autenticacion.usuarios.dto.ApiResponse;
import co.edu.unicauca.autenticacion.usuarios.security.JwtUtil;
import co.edu.unicauca.autenticacion.usuarios.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final GoogleAuthService googleAuthService;
	private final JwtUtil jwtUtil;

	@PostMapping("/google")
	public ResponseEntity<ApiResponse<JwtResponse>> authenticateWithGoogle(
		@RequestBody GoogleTokenRequest googleTokenRequest) {

		GoogleIdToken.Payload payload = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

		String email = payload.getEmail();
		String nombreUsuario = email.split("@")[0];

		String jwtTokenBearer = jwtUtil.generateJwtToken(email, nombreUsuario);

		JwtResponse jwtResponse = new JwtResponse(jwtTokenBearer);

		ApiResponse<JwtResponse> apiResponse = new ApiResponse<>(200, "Inicio de sesión exitoso", jwtResponse);

		return ResponseEntity.ok(apiResponse);
	}
}
