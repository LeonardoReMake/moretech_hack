package ru.moretech.moretech_server.work_with_vtb_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;

import java.util.Collections;
import java.util.HashMap;

public class RecognitionApi {

    @Value("${ibmClientId}")
    private String imbClientId;

    @Value("${recognitionUrl}")
    private String recognitionApiUrl;

    public CarResponse getCarResponse(String image) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-ibm-client-id", imbClientId);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("content", image);
        

        HttpEntity<?> entity = new HttpEntity<Object>(body, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> exchange =
                restTemplate.exchange(recognitionApiUrl, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(exchange.getBody(), CarResponse.class);
    }
}
