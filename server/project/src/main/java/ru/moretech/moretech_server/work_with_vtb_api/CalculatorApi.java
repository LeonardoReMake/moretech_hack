package ru.moretech.moretech_server.work_with_vtb_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.paymentsGraph.GraphRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.paymentsGraph.GraphResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.settings.SettingsResponse;

import java.util.Collections;

public class CalculatorApi {

    @Value("${calculatorCalculateUrl}")
    private String calculateUrl;

    @Value("${calculatorPaymentsGraphUrl}")
    private String paymentsGraphUrl;

    @Value("${calculatorSettingsUrl}")
    private String settingsUrl;

    @Value("${ibmClientId}")
    private String ibmClientId;

    public SettingsResponse getSettings(String name, String language) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-ibm-client-id", ibmClientId);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(settingsUrl)
                .queryParam("name", name)
                .queryParam("language", language);


        ResponseEntity<String> exchange =
                restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, entity,
                        String.class);

        System.out.println();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(exchange.getBody(), SettingsResponse.class);
    }

    public CalculateResponse postCalculate(CalculateRequest request) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-ibm-client-id", ibmClientId);

        HttpEntity<CalculateRequest> entity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<String> exchange =
                restTemplate.exchange(calculateUrl, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(exchange.getBody(), CalculateResponse.class);
    }

}
