package co.edu.unicauca.autenticacion.usuarios.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleAuthService {
    GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString);
}

