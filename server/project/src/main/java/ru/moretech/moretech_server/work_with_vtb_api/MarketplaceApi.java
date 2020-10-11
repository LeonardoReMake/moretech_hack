package ru.moretech.moretech_server.work_with_vtb_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarBrand;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarModel;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.Marketplace;

import java.util.Collections;


public class MarketplaceApi {

    @Value("${ibmClientId}")
    private String ibmClientId;

    @Value("${marketplaceUrl}")
    private String marketplaceApiUrl;

    private Marketplace marketplace = null;

    public Marketplace getMarketplace() throws JsonProcessingException {
        if (marketplace == null) {
            updateMarketplace();
        }
        return marketplace;
    }

    public void updateMarketplace() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-ibm-client-id", ibmClientId);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> exchange =
                restTemplate.exchange(marketplaceApiUrl, HttpMethod.GET, entity,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        marketplace = objectMapper.readValue(exchange.getBody(), Marketplace.class);

        for (CarBrand carBrand : marketplace.getList()) {
            if (carBrand.getAlias().equals("skoda")) {
                for (CarModel model : carBrand.getModels()) {
                    model.getBrand().setTitle("SKODA");
                }
            }

            if (carBrand.getAlias().equals("bmw")) {
                for (CarModel model : carBrand.getModels()) {
                    model.setTitle(model.getTitle().split(" ")[0]);
                }
            }

        }
    }
}
