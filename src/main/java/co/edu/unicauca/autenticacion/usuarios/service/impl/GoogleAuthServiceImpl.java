package co.edu.unicauca.autenticacion.usuarios.service.impl;

import co.edu.unicauca.autenticacion.usuarios.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import co.edu.unicauca.autenticacion.usuarios.exception.CustomException;

import java.util.Collections;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Override
    public GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new CustomException("Token inválido o expirado", HttpStatus.UNAUTHORIZED);
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new CustomException("Error verificando token de Google", HttpStatus.UNAUTHORIZED);
        }
    }
}
