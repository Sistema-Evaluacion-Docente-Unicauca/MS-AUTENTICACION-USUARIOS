package co.edu.unicauca.autenticacion.usuarios.service.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.edu.unicauca.autenticacion.usuarios.dto.KiraResponseDTO;
import co.edu.unicauca.autenticacion.usuarios.service.KiraService;

@Service
@Data
public class KiraServiceImpl implements KiraService {

    @Value("${kira.api.url}")
    private String kiraApiUrl;

    @Override
    public KiraResponseDTO getUserInfo(String username) {
        // Llamar a la API KIRA para obtener la información del usuario
        RestTemplate restTemplate = new RestTemplate();
        String urlDataTercero = "/labor/dataTercero/";
        String url = kiraApiUrl + urlDataTercero + username;
        ResponseEntity<KiraResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, KiraResponseDTO.class);
        return response.getBody();
    }
    
}