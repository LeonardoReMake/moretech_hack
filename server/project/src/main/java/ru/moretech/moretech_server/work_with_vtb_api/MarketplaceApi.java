package ru.moretech.moretech_server.work_with_vtb_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.moretech.moretech_server.Entities.CarBrand;
import ru.moretech.moretech_server.Entities.Marketplace;

import javax.security.auth.login.CredentialExpiredException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MarketplaceApi {

    @Value("${ibmClientId}")
    private String ibmClientId;

    @Value("${marketplaceUrl}")
    private String marketplaceApiUrl;

    public Marketplace getMarketplace() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-ibm-client-id", ibmClientId);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> exchange =
                restTemplate.exchange(marketplaceApiUrl, HttpMethod.GET, entity,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(exchange.getBody(), Marketplace.class);
    }


}
